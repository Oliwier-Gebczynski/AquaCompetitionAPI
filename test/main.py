#!/usr/bin/env python3
import os
import sys
import requests
import argparse
import time
from datetime import datetime

# ----------------------------------------
# Konfiguracja przez argparse i/lub ENV
# ----------------------------------------
def parse_args():
    parser = argparse.ArgumentParser(description="Seedowanie AquaCompetition API danymi testowymi przez POSTy z obsługą błędów")
    parser.add_argument(
        "--base-url",
        type=str,
        default=os.getenv('BASE_URL', 'http://localhost:8080/api'),
        help="Base URL API, np. http://localhost:8080/api"
    )
    parser.add_argument(
        "--delay",
        type=float,
        default=0.0,
        help="Opcjonalne opóźnienie (w sekundach) pomiędzy żądaniami"
    )
    parser.add_argument(
        "--verbose",
        action="store_true",
        help="Jeśli podane, wypisuje więcej informacji debugowych"
    )
    parser.add_argument(
        "--continue-on-error",
        action="store_true",
        help="Jeśli podane, przy błędzie HTTP przy tworzeniu zasobu loguje i kontynuuje, zamiast przerywać"
    )
    return parser.parse_args()

BASE_URL = None
DELAY = 0.0
VERBOSE = False
CONTINUE_ON_ERROR = False

def log(msg):
    if VERBOSE:
        print("[DEBUG]", msg)

def maybe_delay():
    if DELAY and DELAY > 0:
        time.sleep(DELAY)

def assert_status(response, expected_status):
    """
    Sprawdza, czy response.status_code jest zgodny z expected_status.
    expected_status może być int lub iterable z akceptowanymi kodami.
    Rzuca HTTPError, jeśli niezgodny.
    """
    code = response.status_code
    ok = False
    if isinstance(expected_status, int):
        ok = (code == expected_status)
    else:
        try:
            ok = (code in expected_status)
        except Exception:
            ok = False
    if not ok:
        # W razie 500, 400 itp. rzucamy
        raise requests.exceptions.HTTPError(f"Oczekiwano statusu {expected_status}, otrzymano {code}. Body: {response.text}", response=response)

# ----------------------------------------
# Funkcje tworzące zasoby z obsługą błędów
# ----------------------------------------
def create_competition(name, startDate, endDate, location, organizer):
    url = f"{BASE_URL}/competitions"
    payload = {
        "name": name,
        "startDate": startDate,
        "endDate": endDate,
        "location": location,
        "organizer": organizer
    }
    log(f"POST {url} payload={payload}")
    try:
        resp = requests.post(url, json=payload)
        assert_status(resp, (200, 201))
        data = resp.json()
        comp_id = data.get("id")
        if comp_id is None:
            print(f"❌ Utworzono Competition, ale brak pola id w odpowiedzi: {data}")
            return None
        print(f"Utworzono Competition: id={comp_id}, name='{name}'")
        return comp_id
    except Exception as e:
        print(f"❌ Błąd przy tworzeniu Competition '{name}': {e}")
        if not CONTINUE_ON_ERROR:
            sys.exit(1)
        return None

def create_competitor(firstName, lastName, dateOfBirth, gender, club, category):
    url = f"{BASE_URL}/competitors"
    payload = {
        "firstName": firstName,
        "lastName": lastName,
        "dateOfBirth": dateOfBirth,
        "gender": gender,
        "club": club,
        "category": category
    }
    log(f"POST {url} payload={payload}")
    try:
        resp = requests.post(url, json=payload)
        assert_status(resp, (200, 201))
        data = resp.json()
        comp_id = data.get("id")
        if comp_id is None:
            print(f"❌ Utworzono Competitor, ale brak pola id w odpowiedzi: {data}")
            return None
        print(f"Utworzono Competitor: id={comp_id}, name='{firstName} {lastName}'")
        return comp_id
    except Exception as e:
        print(f"❌ Błąd przy tworzeniu Competitor '{firstName} {lastName}': {e}")
        if not CONTINUE_ON_ERROR:
            sys.exit(1)
        return None

def create_race(competition_id, style, distance, category, gender, dateTime):
    if competition_id is None:
        print("⚠️ Nie można utworzyć Race bez ważnego competition_id (None). Pomijam.")
        return None
    url = f"{BASE_URL}/competitions/{competition_id}/races"
    payload = {
        "style": style,
        "distance": distance,
        "category": category,
        "gender": gender,
        "dateTime": dateTime
    }
    log(f"POST {url} payload={payload}")
    try:
        resp = requests.post(url, json=payload)
        assert_status(resp, (200, 201))
        data = resp.json()
        race_id = data.get("id")
        if race_id is None:
            print(f"❌ Utworzono Race, ale brak pola id w odpowiedzi: {data}")
            return None
        print(f"Utworzono Race: id={race_id}, competition_id={competition_id}, style={style}, distance={distance}")
        return race_id
    except Exception as e:
        print(f"❌ Błąd przy tworzeniu Race dla competition_id={competition_id}: {e}")
        if not CONTINUE_ON_ERROR:
            sys.exit(1)
        return None

def create_result(race_id, competitor_id, time_str, lane, finalPosition, disqualified=False):
    if race_id is None or competitor_id is None:
        print("⚠️ Nie można utworzyć Result bez ważnych race_id lub competitor_id (None). Pomijam.")
        return None
    url = f"{BASE_URL}/races/{race_id}/results"
    payload = {
        "competitor": {"id": competitor_id},
        "time": time_str,
        "lane": lane,
        "finalPosition": finalPosition,
        "disqualified": disqualified
    }
    log(f"POST {url} payload={payload}")
    try:
        resp = requests.post(url, json=payload)
        assert_status(resp, (200, 201))
        data = resp.json()
        res_id = data.get("id")
        if res_id is None:
            print(f"❌ Utworzono Result, ale brak pola id w odpowiedzi: {data}")
            return None
        print(f"Utworzono Result: id={res_id}, race_id={race_id}, competitor_id={competitor_id}, time={time_str}")
        return res_id
    except Exception as e:
        print(f"❌ Błąd przy tworzeniu Result dla race_id={race_id}, competitor_id={competitor_id}: {e}")
        if not CONTINUE_ON_ERROR:
            sys.exit(1)
        return None

# ----------------------------------------
# Główna logika seedowania z obsługą None
# ----------------------------------------
def main():
    global BASE_URL, DELAY, VERBOSE, CONTINUE_ON_ERROR
    args = parse_args()
    BASE_URL = args.base_url.rstrip('/')
    DELAY = args.delay
    VERBOSE = args.verbose
    CONTINUE_ON_ERROR = args.continue_on_error

    print(f"Base URL API: {BASE_URL}")
    if DELAY > 0:
        print(f"Opóźnienie między żądaniami: {DELAY} s")
    if VERBOSE:
        print("Tryb verbose ON")
    if CONTINUE_ON_ERROR:
        print("Tryb continue-on-error ON: przy błędzie HTTP będę logować i kontynuować.")

    # 1. Tworzymy Competitions
    comp1_id = create_competition(
        name="Mistrzostwa Testowe 2025",
        startDate="2025-07-01",
        endDate="2025-07-05",
        location="Warszawa",
        organizer="PT Pływanie"
    )
    maybe_delay()
    comp2_id = create_competition(
        name="Puchar Lata 2025",
        startDate="2025-08-10",
        endDate="2025-08-12",
        location="Kraków",
        organizer="Organizator X"
    )
    maybe_delay()

    # 2. Tworzymy Competitors
    comp_jan = create_competitor("Jan", "Kowalski",   "1990-05-15", "M", "Klub A",   "Senior")
    maybe_delay()
    comp_anna = create_competitor("Anna", "Nowak",      "1995-03-20", "F", "Klub B",   "Junior")
    maybe_delay()
    comp_piotr = create_competitor("Piotr", "Wiśniewski","1988-11-02", "M", "Klub C",   "Senior")
    maybe_delay()
    comp_ewa = create_competitor("Ewa", "Zielińska",    "2000-07-12", "F", "Klub D",   "Senior")
    maybe_delay()

    # 3. Tworzymy Races
    race1_id = create_race(
        competition_id=comp1_id,
        style="Freestyle",
        distance=100,
        category="Senior",
        gender="M",
        dateTime="2025-07-02T10:00:00"
    )
    maybe_delay()
    race2_id = create_race(
        competition_id=comp1_id,
        style="Backstroke",
        distance=200,
        category="Senior",
        gender="F",
        dateTime="2025-07-03T11:30:00"
    )
    maybe_delay()
    race3_id = create_race(
        competition_id=comp2_id,
        style="Butterfly",
        distance=50,
        category="Junior",
        gender="M",
        dateTime="2025-08-10T09:00:00"
    )
    maybe_delay()

    # 4. Tworzymy Results
    res1_id = create_result(race1_id, comp_jan,  "00:01:05.00", lane=2, finalPosition=1, disqualified=False)
    maybe_delay()
    res2_id = create_result(race1_id, comp_piotr, "00:01:10.00", lane=3, finalPosition=2, disqualified=False)
    maybe_delay()

    res3_id = create_result(race2_id, comp_anna, "00:02:30.00", lane=1, finalPosition=2, disqualified=False)
    maybe_delay()
    res4_id = create_result(race2_id, comp_ewa,  "00:02:25.00", lane=2, finalPosition=1, disqualified=False)
    maybe_delay()

    res5_id = create_result(race3_id, comp_piotr, "00:00:30.00", lane=1, finalPosition=1, disqualified=False)
    maybe_delay()

    # 5. Podsumowanie - tylko tych, które faktycznie mają ID != None
    print("\n=== Podsumowanie utworzonych zasobów (tylko utworzone) ===")
    if comp1_id: print(f"Competition 1: id={comp1_id}, name='Mistrzostwa Testowe 2025'")
    if comp2_id: print(f"Competition 2: id={comp2_id}, name='Puchar Lata 2025'")
    if comp_jan: print(f"Competitor Jan: id={comp_jan}")
    if comp_anna: print(f"Competitor Anna: id={comp_anna}")
    if comp_piotr: print(f"Competitor Piotr: id={comp_piotr}")
    if comp_ewa: print(f"Competitor Ewa: id={comp_ewa}")
    if race1_id: print(f"Race1: id={race1_id} (comp1)")
    if race2_id: print(f"Race2: id={race2_id} (comp1)")
    if race3_id: print(f"Race3: id={race3_id} (comp2)")
    if res1_id: print(f"Result1: id={res1_id} (race1, Jan)")
    if res2_id: print(f"Result2: id={res2_id} (race1, Piotr)")
    if res3_id: print(f"Result3: id={res3_id} (race2, Anna)")
    if res4_id: print(f"Result4: id={res4_id} (race2, Ewa)")
    if res5_id: print(f"Result5: id={res5_id} (race3, Piotr)")
    print("=== Seedowanie (przynajmniej próbę) zakończone ===\n")

    print("Możesz w Postmanie testować utworzone zasoby (jeśli zostały utworzone):")
    if comp1_id:
        print(f"  GET {BASE_URL}/competitions/{comp1_id}")
        print(f"  GET {BASE_URL}/competitions/{comp1_id}/races")
    if comp_jan:
        print(f"  GET {BASE_URL}/competitors/{comp_jan}")
    if race1_id:
        print(f"  GET {BASE_URL}/races/{race1_id}")
        print(f"  GET {BASE_URL}/results/winner/{race1_id}")
    if comp1_id:
        print(f"  GET {BASE_URL}/results/medallists/{comp1_id}")

if __name__ == '__main__':
    main()
