# Aqua Competition API Documentation

## Overview
This API manages aquatic competitions, competitors, races, and results with comprehensive CRUD operations and advanced features like medal tables and personal bests.

## Base URL
```
http://localhost:8080/api
```

## Data Models

### Competitor
```json
{
    "id": 1,
    "firstName": "John",
    "lastName": "Smith",
    "dateOfBirth": "1995-06-15",
    "gender": "MALE",
    "club": "Aquatic Club",
    "category": "SENIOR"
}
```

### Competition
```json
{
    "id": 1,
    "name": "Summer Championships 2025",
    "startDate": "2025-07-15",
    "endDate": "2025-07-17",
    "location": "Olympic Pool Complex",
    "organizer": "National Swimming Federation"
}
```

### Race
```json
{
    "id": 1,
    "style": "FREESTYLE",
    "distance": 100,
    "category": "SENIOR",
    "gender": "MALE",
    "dateTime": "2025-07-15T10:30:00"
}
```

### Result
```json
{
    "id": 1,
    "time": "00:50.25",
    "lane": 4,
    "finalPosition": 1,
    "disqualified": false,
    "competitor": {
        "id": 1
    },
    "race": {
        "id": 1
    }
}
```

---

## Competitors Endpoints

### 1. Get All Competitors
**GET** `/competitors`

Retrieves all competitors from the system.

#### Postman Setup
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/competitors`
- **Headers:** `Content-Type: application/json`

#### Expected Response (200 OK)
```json
[
    {
        "id": 1,
        "firstName": "John",
        "lastName": "Smith",
        "dateOfBirth": "1995-06-15T00:00:00.000+00:00",
        "gender": "MALE",
        "club": "Aquatic Club",
        "category": "SENIOR"
    },
    {
        "id": 2,
        "firstName": "Jane",
        "lastName": "Doe",
        "dateOfBirth": "1998-03-22T00:00:00.000+00:00",
        "gender": "FEMALE",
        "club": "Swimming Stars",
        "category": "SENIOR"
    }
]
```

### 2. Get Single Competitor
**GET** `/competitors/{id}`

#### Postman Setup
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/competitors/1`

#### Expected Response (200 OK)
```json
{
    "id": 1,
    "firstName": "John",
    "lastName": "Smith",
    "dateOfBirth": "1995-06-15T00:00:00.000+00:00",
    "gender": "MALE",
    "club": "Aquatic Club",
    "category": "SENIOR"
}
```

#### Error Response (404 Not Found)
```json
{
    "timestamp": "2025-06-15T10:30:00.000+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Competitor not found with id 999"
}
```

### 3. Create New Competitor
**POST** `/competitors`

#### Postman Setup
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/competitors`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
    "firstName": "Michael",
    "lastName": "Johnson",
    "dateOfBirth": "1992-08-10",
    "gender": "MALE",
    "club": "Elite Swimmers",
    "category": "SENIOR"
}
```

#### Expected Response (200 OK)
```json
{
    "id": 3,
    "firstName": "Michael",
    "lastName": "Johnson",
    "dateOfBirth": "1992-08-10T00:00:00.000+00:00",
    "gender": "MALE",
    "club": "Elite Swimmers",
    "category": "SENIOR"
}
```

### 4. Update Competitor
**PUT** `/competitors/{id}`

#### Postman Setup
- **Method:** `PUT`
- **URL:** `http://localhost:8080/api/competitors/1`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
    "firstName": "John",
    "lastName": "Smith Jr.",
    "dateOfBirth": "1995-06-15",
    "gender": "MALE",
    "club": "New Aquatic Club",
    "category": "SENIOR"
}
```

### 5. Delete Competitor
**DELETE** `/competitors/{id}`

#### Postman Setup
- **Method:** `DELETE`
- **URL:** `http://localhost:8080/api/competitors/1`

#### Expected Response (204 No Content)
No response body.

### 6. Get Competitor's Personal Bests
**GET** `/competitors/{competitorId}/personal-bests`

#### Postman Setup
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/competitors/1/personal-bests`

#### Expected Response (200 OK)
```json
[
    {
        "style": "FREESTYLE",
        "distance": 100,
        "category": "SENIOR",
        "gender": "MALE",
        "bestTime": "00:50.25",
        "competitionName": "Summer Championships 2025",
        "raceDate": "2025-07-15"
    },
    {
        "style": "BUTTERFLY",
        "distance": 100,
        "category": "SENIOR",
        "gender": "MALE",
        "bestTime": "00:55.80",
        "competitionName": "Winter Open 2025",
        "raceDate": "2025-02-20"
    }
]
```

---

## Competitions Endpoints

### 7. Get All Competitions
**GET** `/competitions`

#### Postman Setup
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/competitions`

### 8. Create New Competition
**POST** `/competitions`

#### Postman Setup
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/competitions`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
    "name": "Spring Championships 2025",
    "startDate": "2025-05-15",
    "endDate": "2025-05-17",
    "location": "City Aquatic Center",
    "organizer": "Regional Swimming Association"
}
```

### 9. Get Competition Races
**GET** `/competitions/{competitionId}/races`

#### Postman Setup
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/competitions/1/races`

### 10. Create Race for Competition
**POST** `/competitions/{competitionId}/races`

#### Postman Setup
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/competitions/1/races`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
    "style": "FREESTYLE",
    "distance": 100,
    "category": "SENIOR",
    "gender": "MALE",
    "dateTime": "2025-07-15T10:30:00"
}
```

### 11. Get Competition Medal Table
**GET** `/competitions/{competitionId}/medal-table`

#### Postman Setup
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/competitions/1/medal-table`

#### Expected Response (200 OK)
```json
[
    {
        "competitor": {
            "id": 1,
            "firstName": "John",
            "lastName": "Smith",
            "dateOfBirth": "1995-06-15",
            "gender": "MALE",
            "club": "Aquatic Club",
            "category": "SENIOR"
        },
        "goldMedals": 3,
        "silverMedals": 1,
        "bronzeMedals": 0,
        "totalMedals": 4
    },
    {
        "competitor": {
            "id": 2,
            "firstName": "Jane",
            "lastName": "Doe",
            "dateOfBirth": "1998-03-22",
            "gender": "FEMALE",
            "club": "Swimming Stars",
            "category": "SENIOR"
        },
        "goldMedals": 2,
        "silverMedals": 2,
        "bronzeMedals": 1,
        "totalMedals": 5
    }
]
```

---

## Races Endpoints

### 12. Get All Races
**GET** `/races`

### 13. Get Race by ID
**GET** `/races/{id}`

### 14. Create Race Result
**POST** `/races/{id}/results`

#### Postman Setup
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/races/1/results`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
    "time": "00:51.45",
    "lane": 3,
    "finalPosition": 2,
    "disqualified": false,
    "competitor": {
        "id": 2
    }
}
```

### 15. Get Race Results
**GET** `/races/{id}/results`

#### Postman Setup
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/races/1/results`

### 16. Get Race Standings
**GET** `/races/{raceId}/standings`

#### Postman Setup
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/races/1/standings`

#### Expected Response (200 OK)
```json
[
    {
        "position": 1,
        "competitor": {
            "id": 1,
            "firstName": "John",
            "lastName": "Smith",
            "dateOfBirth": "1995-06-15",
            "gender": "MALE",
            "club": "Aquatic Club",
            "category": "SENIOR"
        },
        "time": "00:50.25",
        "lane": 4,
        "disqualified": false,
        "status": "FINISHED"
    },
    {
        "position": 2,
        "competitor": {
            "id": 2,
            "firstName": "Jane",
            "lastName": "Doe",
            "dateOfBirth": "1998-03-22",
            "gender": "FEMALE",
            "club": "Swimming Stars",
            "category": "SENIOR"
        },
        "time": "00:51.45",
        "lane": 3,
        "disqualified": false,
        "status": "FINISHED"
    }
]
```

---

## Results Endpoints

### 17. Get All Results
**GET** `/results`

### 18. Create Result
**POST** `/results`

#### Postman Setup
- **Method:** `POST`
- **URL:** `http://localhost:8080/api/results`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
    "time": "00:52.10",
    "lane": 5,
    "finalPosition": 3,
    "disqualified": false,
    "competitor": {
        "id": 3
    },
    "race": {
        "id": 1
    }
}
```

### 19. Update Result
**PUT** `/results/{id}`

#### Postman Setup
- **Method:** `PUT`
- **URL:** `http://localhost:8080/api/results/1`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
    "time": "00:50.15",
    "lane": 4,
    "finalPosition": 1,
    "disqualified": false,
    "competitor": {
        "id": 1
    },
    "race": {
        "id": 1
    }
}
```

### 20. Get Race Winner
**GET** `/results/winner/{raceId}`

#### Postman Setup
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/results/winner/1`

### 21. Get Competition Medallists
**GET** `/results/medallists/{competitionId}`

#### Postman Setup
- **Method:** `GET`
- **URL:** `http://localhost:8080/api/results/medallists/1`

---

## Usage Scenarios

### Scenario 1: Setting up a New Competition
1. **Create Competition** - POST `/competitions`
2. **Create Races** - POST `/competitions/{id}/races` for each race
3. **Register Competitors** - POST `/competitors` if not existing
4. **Add Results** - POST `/races/{id}/results` for each competitor

### Scenario 2: Viewing Competition Results
1. **Get Medal Table** - GET `/competitions/{id}/medal-table`
2. **Get Race Standings** - GET `/races/{id}/standings`
3. **Get Individual Results** - GET `/results/competitor/{id}`

### Scenario 3: Managing Competitor Data
1. **Add New Competitor** - POST `/competitors`
2. **View Personal Bests** - GET `/competitors/{id}/personal-bests`
3. **Update Competitor Info** - PUT `/competitors/{id}`

## Error Handling

All endpoints return appropriate HTTP status codes:
- **200 OK** - Successful GET/PUT requests
- **201 Created** - Successful POST requests
- **204 No Content** - Successful DELETE requests
- **400 Bad Request** - Invalid request data
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server errors

## Testing Tips

1. **Start with basic CRUD operations** before testing complex scenarios
2. **Always create dependencies first** (competition before races, competitors before results)
3. **Use proper date formats** - "yyyy-MM-dd" for dates, "yyyy-MM-dd'T'HH:mm:ss" for timestamps
4. **Verify foreign key relationships** exist before creating dependent entities
5. **Test error scenarios** with invalid IDs and malformed data