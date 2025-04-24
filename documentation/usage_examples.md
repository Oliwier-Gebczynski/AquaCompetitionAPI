# Competition Management System - Usage Examples

These examples demonstrate the typical usage flow of the competition management system, which can be used as a basis for defining RESTful endpoints.

## 1. Competition Management

### 1.1 Creating a New Competition

**Use Case**: An organizer wants to create a new swimming competition.

**Example Data**:
```json
{
  "name": "Summer Swimming Championship 2025",
  "startDate": "2025-07-15",
  "endDate": "2025-07-20",
  "location": "Municipal Olympic Pool",
  "organizer": "City Swimming Association"
}
```

**Expected Endpoint**: `POST /api/competitions`

### 1.2 Viewing All Competitions

**Use Case**: A user wants to see all upcoming and past competitions.

**Expected Endpoint**: `GET /api/competitions`

### 1.3 Viewing Competition Details

**Use Case**: A user wants to see detailed information about a specific competition.

**Expected Endpoint**: `GET /api/competitions/{competitionId}`

### 1.4 Updating Competition Information

**Use Case**: An organizer needs to change the dates or location of a competition.

**Example Data**:
```json
{
  "name": "Summer Swimming Championship 2025",
  "startDate": "2025-07-18",  // Changed date
  "endDate": "2025-07-23",    // Changed date
  "location": "National Olympic Center",  // Changed location
  "organizer": "City Swimming Association"
}
```

**Expected Endpoint**: `PUT /api/competitions/{competitionId}`

### 1.5 Cancelling a Competition

**Use Case**: An organizer needs to cancel a planned competition.

**Expected Endpoint**: `DELETE /api/competitions/{competitionId}`

## 2. Race Management

### 2.1 Creating a New Race

**Use Case**: An organizer wants to add a new race to a competition.

**Example Data**:
```json
{
  "competition": {"id": 1},
  "style": "Butterfly",
  "distance": 200,
  "category": "Senior",
  "gender": "Female",
  "dateTime": "2025-07-16T14:30:00"
}
```

**Expected Endpoint**: `POST /api/races` or `POST /api/competitions/{competitionId}/races`

### 2.2 Viewing All Races in a Competition

**Use Case**: A user wants to see all races scheduled for a particular competition.

**Expected Endpoint**: `GET /api/competitions/{competitionId}/races`

### 2.3 Viewing Race Details

**Use Case**: A user wants to see detailed information about a specific race.

**Expected Endpoint**: `GET /api/races/{raceId}`

### 2.4 Updating Race Information

**Use Case**: An organizer needs to change the time or details of a race.

**Example Data**:
```json
{
  "competition": {"id": 1},
  "style": "Butterfly",
  "distance": 200,
  "category": "Senior",
  "gender": "Female",
  "dateTime": "2025-07-16T16:00:00"  // Changed time
}
```

**Expected Endpoint**: `PUT /api/races/{raceId}`

### 2.5 Cancelling a Race

**Use Case**: An organizer needs to remove a race from the competition schedule.

**Expected Endpoint**: `DELETE /api/races/{raceId}`

## 3. Competitor Management

### 3.1 Registering a New Competitor

**Use Case**: A competitor wants to register in the system.

**Example Data**:
```json
{
  "firstName": "Sarah",
  "lastName": "Johnson",
  "dateOfBirth": "1997-09-23",
  "gender": "Female",
  "club": "Dolphin Swimmers",
  "category": "Senior"
}
```

**Expected Endpoint**: `POST /api/competitors`

### 3.2 Viewing All Competitors

**Use Case**: An organizer wants to see all registered competitors.

**Expected Endpoint**: `GET /api/competitors`

### 3.3 Searching for Competitors

**Use Case**: An organizer wants to find competitors matching certain criteria.

**Expected Endpoint**: `GET /api/competitors?club=Dolphin%20Swimmers` or `GET /api/competitors?category=Senior`

### 3.4 Viewing Competitor Details

**Use Case**: An organizer wants to see detailed information about a specific competitor.

**Expected Endpoint**: `GET /api/competitors/{competitorId}`

### 3.5 Updating Competitor Information

**Use Case**: A competitor needs to update their personal information.

**Example Data**:
```json
{
  "firstName": "Sarah",
  "lastName": "Johnson",
  "dateOfBirth": "1997-09-23",
  "gender": "Female",
  "club": "Elite Swimmers",  // Changed club
  "category": "Senior"
}
```

**Expected Endpoint**: `PUT /api/competitors/{competitorId}`

### 3.6 Removing a Competitor

**Use Case**: A competitor wants to be removed from the system.

**Expected Endpoint**: `DELETE /api/competitors/{competitorId}`

## 4. Result Management

### 4.1 Recording a Race Result

**Use Case**: After a race is completed, an official needs to record the result for a competitor.

**Example Data**:
```json
{
  "competitor": {"id": 2},
  "race": {"id": 3},
  "time": "00:02:15.430",  // 2 minutes, 15.43 seconds
  "lane": 4,
  "finalPosition": 1,
  "disqualified": false
}
```

**Expected Endpoint**: `POST /api/results` or `POST /api/races/{raceId}/results`

### 4.2 Viewing Results for a Race

**Use Case**: A user wants to see all results from a specific race.

**Expected Endpoint**: `GET /api/races/{raceId}/results`

### 4.3 Viewing Results for a Competitor

**Use Case**: A user wants to see all results achieved by a specific competitor.

**Expected Endpoint**: `GET /api/competitors/{competitorId}/results`

### 4.4 Updating a Result

**Use Case**: An official needs to correct an error in a recorded result.

**Example Data**:
```json
{
  "competitor": {"id": 2},
  "race": {"id": 3},
  "time": "00:02:14.830",  // Corrected time
  "lane": 4,
  "finalPosition": 1,
  "disqualified": false
}
```

**Expected Endpoint**: `PUT /api/results/{resultId}`

### 4.5 Removing a Result

**Use Case**: An official needs to remove an incorrectly entered result.

**Expected Endpoint**: `DELETE /api/results/{resultId}`

## 5. Advanced Features

### 5.1 Race Standings

**Use Case**: A user wants to see the current standings/rankings for a race.

**Expected Endpoint**: `GET /api/races/{raceId}/standings`

### 5.2 Competition Medal Table

**Use Case**: A user wants to see the medal table for a competition.

**Expected Endpoint**: `GET /api/competitions/{competitionId}/medal-table`

### 5.3 Personal Best Times

**Use Case**: A competitor wants to see their personal best times in different events.

**Expected Endpoint**: `GET /api/competitors/{competitorId}/personal-bests`

### 5.4 Race History for a Category

**Use Case**: A user wants to see historical race results for a particular category and style.

**Expected Endpoint**: `GET /api/races/history?style=Butterfly&distance=100&gender=Male`

### 5.5 Competitor Registration for a Race

**Use Case**: A competitor wants to register for a specific race.

**Example Data**:
```json
{
  "competitorId": 5,
  "lanePreference": 4
}
```

**Expected Endpoint**: `POST /api/races/{raceId}/registrations`

### 5.6 Competitor Race Schedule

**Use Case**: A competitor wants to see their race schedule for a competition.

**Expected Endpoint**: `GET /api/competitors/{competitorId}/schedule?competitionId=2`