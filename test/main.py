#!/usr/bin/env python3
import os
import sys
import requests
import argparse
import time
import json
from datetime import datetime, timedelta
from typing import Optional, List, Dict, Any


# ----------------------------------------
# Konfiguracja przez argparse i/lub ENV
# ----------------------------------------
def parse_args():
    parser = argparse.ArgumentParser(
        description="Seedowanie AquaCompetition API danymi testowymi i testowanie endpointów")
    parser.add_argument(
        "--base-url",
        type=str,
        default=os.getenv('BASE_URL', 'http://localhost:8080/api'),
        help="Base URL API, np. http://localhost:8080/api"
    )
    parser.add_argument(
        "--delay",
        type=float,
        default=0.2,
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
    parser.add_argument(
        "--test-only",
        action="store_true",
        help="Tylko testuje endpointy, nie tworzy nowych danych"
    )
    parser.add_argument(
        "--seed-only",
        action="store_true",
        help="Tylko seeduje dane, nie testuje endpointów"
    )
    return parser.parse_args()


BASE_URL = None
DELAY = 0.0
VERBOSE = False
CONTINUE_ON_ERROR = False

# Przechowywanie utworzonych ID
created_resources = {
    'competitions': [],
    'competitors': [],
    'races': [],
    'results': []
}


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
        raise requests.exceptions.HTTPError(
            f"Oczekiwano statusu {expected_status}, otrzymano {code}. Body: {response.text}", response=response)


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
        print(f"✅ Utworzono Competition: id={comp_id}, name='{name}'")
        created_resources['competitions'].append(comp_id)
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
        print(f"✅ Utworzono Competitor: id={comp_id}, name='{firstName} {lastName}'")
        created_resources['competitors'].append(comp_id)
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
        print(f"✅ Utworzono Race: id={race_id}, competition_id={competition_id}, style={style}, distance={distance}")
        created_resources['races'].append(race_id)
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
        print(f"✅ Utworzono Result: id={res_id}, race_id={race_id}, competitor_id={competitor_id}, time={time_str}")
        created_resources['results'].append(res_id)
        return res_id
    except Exception as e:
        print(f"❌ Błąd przy tworzeniu Result dla race_id={race_id}, competitor_id={competitor_id}: {e}")
        if not CONTINUE_ON_ERROR:
            sys.exit(1)
        return None


# ----------------------------------------
# Funkcje testujące endpointy
# ----------------------------------------
def test_endpoint(method: str, url: str, expected_status=200, payload=None, description=""):
    """Testuje pojedynczy endpoint"""
    try:
        log(f"Testing {method} {url}")
        if method.upper() == "GET":
            resp = requests.get(url)
        elif method.upper() == "POST":
            resp = requests.post(url, json=payload)
        elif method.upper() == "PUT":
            resp = requests.put(url, json=payload)
        elif method.upper() == "DELETE":
            resp = requests.delete(url)
        else:
            print(f"❌ Nieobsługiwana metoda: {method}")
            return False

        if resp.status_code == expected_status:
            print(f"✅ {method} {url} - Status: {resp.status_code} {description}")
            if VERBOSE and resp.text:
                try:
                    data = resp.json()
                    print(f"   Response: {json.dumps(data, indent=2)}")
                except:
                    print(f"   Response: {resp.text[:200]}...")
            return True
        else:
            print(f"❌ {method} {url} - Oczekiwano: {expected_status}, otrzymano: {resp.status_code}")
            print(f"   Response: {resp.text[:200]}...")
            return False
    except Exception as e:
        print(f"❌ {method} {url} - Błąd: {e}")
        return False


def test_all_endpoints():
    """Testuje wszystkie endpointy API"""
    print("\n=== TESTOWANIE WSZYSTKICH ENDPOINTÓW ===")

    # Pobieramy istniejące dane do testów
    competitions = created_resources['competitions']
    competitors = created_resources['competitors']
    races = created_resources['races']
    results = created_resources['results']

    total_tests = 0
    passed_tests = 0

    # Testy Competition endpoints
    print("\n--- Competition Endpoints ---")
    total_tests += 1
    if test_endpoint("GET", f"{BASE_URL}/competitions", description="(Lista wszystkich zawodów)"):
        passed_tests += 1
    maybe_delay()

    if competitions:
        comp_id = competitions[0]
        total_tests += 1
        if test_endpoint("GET", f"{BASE_URL}/competitions/{comp_id}", description=f"(Szczegóły zawodów {comp_id})"):
            passed_tests += 1
        maybe_delay()

        total_tests += 1
        if test_endpoint("GET", f"{BASE_URL}/competitions/{comp_id}/races", description=f"(Wyścigi zawodów {comp_id})"):
            passed_tests += 1
        maybe_delay()

    # Testy Competitor endpoints
    print("\n--- Competitor Endpoints ---")
    total_tests += 1
    if test_endpoint("GET", f"{BASE_URL}/competitors", description="(Lista wszystkich zawodników)"):
        passed_tests += 1
    maybe_delay()

    if competitors:
        comp_id = competitors[0]
        total_tests += 1
        if test_endpoint("GET", f"{BASE_URL}/competitors/{comp_id}", description=f"(Szczegóły zawodnika {comp_id})"):
            passed_tests += 1
        maybe_delay()

    # Testy Race endpoints
    print("\n--- Race Endpoints ---")
    total_tests += 1
    if test_endpoint("GET", f"{BASE_URL}/races", description="(Lista wszystkich wyścigów)"):
        passed_tests += 1
    maybe_delay()

    if races:
        race_id = races[0]
        total_tests += 1
        if test_endpoint("GET", f"{BASE_URL}/races/{race_id}", description=f"(Szczegóły wyścigu {race_id})"):
            passed_tests += 1
        maybe_delay()

        total_tests += 1
        if test_endpoint("GET", f"{BASE_URL}/races/{race_id}/results", description=f"(Wyniki wyścigu {race_id})"):
            passed_tests += 1
        maybe_delay()

    # Testy Result endpoints
    print("\n--- Result Endpoints ---")
    total_tests += 1
    if test_endpoint("GET", f"{BASE_URL}/results", description="(Lista wszystkich wyników)"):
        passed_tests += 1
    maybe_delay()

    if results:
        result_id = results[0]
        total_tests += 1
        if test_endpoint("GET", f"{BASE_URL}/results/{result_id}", description=f"(Szczegóły wyniku {result_id})"):
            passed_tests += 1
        maybe_delay()

    if races:
        race_id = races[0]
        total_tests += 1
        if test_endpoint("GET", f"{BASE_URL}/results/winner/{race_id}", description=f"(Zwycięzca wyścigu {race_id})"):
            passed_tests += 1
        maybe_delay()

    if competitions:
        comp_id = competitions[0]
        total_tests += 1
        if test_endpoint("GET", f"{BASE_URL}/results/medallists/{comp_id}",
                         description=f"(Medaliści zawodów {comp_id})"):
            passed_tests += 1
        maybe_delay()

    # Testy błędnych endpointów (404)
    print("\n--- Testy błędnych endpointów (404) ---")
    total_tests += 1
    if test_endpoint("GET", f"{BASE_URL}/competitions/99999", expected_status=404,
                     description="(Nieistniejące zawody)"):
        passed_tests += 1
    maybe_delay()

    total_tests += 1
    if test_endpoint("GET", f"{BASE_URL}/competitors/99999", expected_status=404,
                     description="(Nieistniejący zawodnik)"):
        passed_tests += 1
    maybe_delay()

    total_tests += 1
    if test_endpoint("GET", f"{BASE_URL}/races/99999", expected_status=404, description="(Nieistniejący wyścig)"):
        passed_tests += 1
    maybe_delay()

    total_tests += 1
    if test_endpoint("GET", f"{BASE_URL}/results/99999", expected_status=404, description="(Nieistniejący wynik)"):
        passed_tests += 1
    maybe_delay()

    # Podsumowanie testów
    print(f"\n=== PODSUMOWANIE TESTÓW ===")
    print(f"Wykonane testy: {total_tests}")
    print(f"Testy zaliczone: {passed_tests}")
    print(f"Testy niezaliczone: {total_tests - passed_tests}")
    print(f"Procent sukcesu: {(passed_tests / total_tests) * 100:.1f}%")


# ----------------------------------------
# Rozszerzone dane testowe
# ----------------------------------------
def seed_comprehensive_data():
    """Tworzy komprehensywny zestaw danych testowych"""
    print("\n=== SEEDOWANIE ROZSZERZONYCH DANYCH TESTOWYCH ===")

    # 1. Zawody - więcej różnorodnych zawodów
    competitions_data = [
        ("Mistrzostwa Polski Seniorów 2025", "2025-07-01", "2025-07-05", "Warszawa", "Polski Związek Pływacki"),
        ("Puchar Europy Juniorów 2025", "2025-08-10", "2025-08-12", "Kraków", "European Aquatics"),
        ("Grand Prix Pływania 2025", "2025-09-15", "2025-09-17", "Gdańsk", "Klub Sportowy Gdańsk"),
        ("Memoriał Adama Kowalskiego", "2025-06-20", "2025-06-22", "Poznań", "UKS Poznań"),
        ("Mistrzostwa Województwa 2025", "2025-05-10", "2025-05-12", "Katowice", "Śląski ZP"),
    ]

    comp_ids = []
    for name, start, end, location, organizer in competitions_data:
        comp_id = create_competition(name, start, end, location, organizer)
        comp_ids.append(comp_id)
        maybe_delay()

    # 2. Zawodnicy - różne kategorie i płcie
    competitors_data = [
        ("Jan", "Kowalski", "1990-05-15", "M", "Klub Warszawa", "Senior"),
        ("Anna", "Nowak", "1995-03-20", "F", "Klub Kraków", "Senior"),
        ("Piotr", "Wiśniewski", "1988-11-02", "M", "Klub Gdańsk", "Senior"),
        ("Ewa", "Zielińska", "2000-07-12", "F", "Klub Poznań", "Senior"),
        ("Marcin", "Kowalczyk", "2005-04-25", "M", "Klub Wrocław", "Junior"),
        ("Karolina", "Lewandowska", "2006-09-08", "F", "Klub Lublin", "Junior"),
        ("Tomasz", "Dąbrowski", "1985-12-03", "M", "Klub Szczecin", "Master"),
        ("Magdalena", "Król", "1992-02-14", "F", "Klub Katowice", "Senior"),
        ("Łukasz", "Mazur", "2004-06-30", "M", "Klub Łódź", "Junior"),
        ("Natalia", "Wojciechowska", "1998-11-17", "F", "Klub Bydgoszcz", "Senior"),
        ("Kamil", "Czarnecki", "2007-01-05", "M", "Klub Olsztyn", "Junior"),
        ("Aleksandra", "Pietrzak", "1987-08-22", "F", "Klub Rzeszów", "Master"),
    ]

    competitor_ids = []
    for firstName, lastName, birth, gender, club, category in competitors_data:
        comp_id = create_competitor(firstName, lastName, birth, gender, club, category)
        competitor_ids.append(comp_id)
        maybe_delay()

    # 3. Wyścigi - różne style i dystanse
    race_configs = [
        ("Freestyle", [50, 100, 200, 400, 800, 1500]),
        ("Backstroke", [50, 100, 200]),
        ("Breaststroke", [50, 100, 200]),
        ("Butterfly", [50, 100, 200]),
        ("Medley", [200, 400]),
    ]

    categories = ["Junior", "Senior", "Master"]
    genders = ["M", "F"]

    race_ids = []
    for comp_id in comp_ids:
        if comp_id is None:
            continue
        race_counter = 0
        for style, distances in race_configs:
            for distance in distances:
                for category in categories:
                    for gender in genders:
                        if race_counter >= 15:  # Ograniczamy liczbę wyścigów na zawody
                            break

                        # Generujemy datę wyścigu
                        base_date = datetime(2025, 7, 1) + timedelta(days=race_counter % 5)
                        race_time = base_date + timedelta(hours=9 + (race_counter % 8))
                        date_str = race_time.strftime("%Y-%m-%dT%H:%M:%S")

                        race_id = create_race(comp_id, style, distance, category, gender, date_str)
                        race_ids.append(race_id)
                        race_counter += 1
                        maybe_delay()
                    if race_counter >= 15:
                        break
                if race_counter >= 15:
                    break
            if race_counter >= 15:
                break

    # 4. Wyniki - generujemy dla każdego wyścigu
    import random

    for race_id in race_ids:
        if race_id is None:
            continue

        # Losujemy 3-6 zawodników na wyścig
        race_competitors = random.sample([c for c in competitor_ids if c is not None],
                                         min(random.randint(3, 6), len([c for c in competitor_ids if c is not None])))

        # Generujemy czasy (w sekundach) i sortujemy
        base_times = sorted([random.uniform(25.0, 180.0) for _ in race_competitors])

        for i, (competitor_id, time_seconds) in enumerate(zip(race_competitors, base_times)):
            # Konwertujemy sekundy na format MM:SS.MS
            minutes = int(time_seconds // 60)
            seconds = time_seconds % 60
            time_str = f"00:{minutes:02d}:{seconds:06.2f}"

            # Losowe dyskwalifikacje (5% szans)
            disqualified = random.random() < 0.05
            final_position = None if disqualified else i + 1

            result_id = create_result(
                race_id,
                competitor_id,
                time_str,
                lane=i + 1,
                finalPosition=final_position,
                disqualified=disqualified
            )
            maybe_delay()


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

    print(f"🏊 AquaCompetition API Seeder & Tester")
    print(f"Base URL API: {BASE_URL}")
    if DELAY > 0:
        print(f"Opóźnienie między żądaniami: {DELAY} s")
    if VERBOSE:
        print("Tryb verbose ON")
    if CONTINUE_ON_ERROR:
        print("Tryb continue-on-error ON: przy błędzie HTTP będę logować i kontynuować.")

    # Sprawdzenie dostępności API
    try:
        resp = requests.get(f"{BASE_URL}/competitions")
        print(f"✅ API dostępne - Status: {resp.status_code}")
    except Exception as e:
        print(f"❌ API niedostępne: {e}")
        sys.exit(1)

    if not args.test_only:
        # Seedowanie danych
        seed_comprehensive_data()

        # Podsumowanie utworzonych zasobów
        print("\n=== PODSUMOWANIE UTWORZONYCH ZASOBÓW ===")
        print(f"Competitions: {len([c for c in created_resources['competitions'] if c is not None])}")
        print(f"Competitors: {len([c for c in created_resources['competitors'] if c is not None])}")
        print(f"Races: {len([r for r in created_resources['races'] if r is not None])}")
        print(f"Results: {len([r for r in created_resources['results'] if r is not None])}")

    if not args.seed_only:
        # Testowanie endpointów
        test_all_endpoints()

    print("\n=== UŻYTECZNE PRZYKŁADY TESTÓW W POSTMAN ===")
    if created_resources['competitions']:
        comp_id = created_resources['competitions'][0]
        print(f"GET {BASE_URL}/competitions/{comp_id}")
        print(f"GET {BASE_URL}/competitions/{comp_id}/races")
    if created_resources['competitors']:
        comp_id = created_resources['competitors'][0]
        print(f"GET {BASE_URL}/competitors/{comp_id}")
    if created_resources['races']:
        race_id = created_resources['races'][0]
        print(f"GET {BASE_URL}/races/{race_id}")
        print(f"GET {BASE_URL}/races/{race_id}/results")
        print(f"GET {BASE_URL}/results/winner/{race_id}")
    if created_resources['competitions']:
        comp_id = created_resources['competitions'][0]
        print(f"GET {BASE_URL}/results/medallists/{comp_id}")

    print("\n🎉 Seedowanie i testowanie zakończone!")


if __name__ == '__main__':
    main()