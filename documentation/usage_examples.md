# Competition Management System - Usage Examples

These examples demonstrate the typical usage flow of the competition management system, serving as a basis for defining RESTful endpoints.

## 1. Competition Management

### 1.1 Creating a New Competition ✅

**Use Case**: An organizer wants to create a new competition.

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

### 1.2 Viewing Competition Details ✅

**Use Case**: A user wants to see detailed information about a specific competition.

**Expected Endpoint**: `GET /api/competitions/{competitionId}`

### 1.3 Updating Competition Information ✅

**Use Case**: An organizer needs to change the details of a competition.

**Example Data**:

```json
{
  "startDate": "2025-07-18",
  "endDate": "2025-07-23",
  "location": "National Olympic Center"
}
```

**Expected Endpoint**: `PUT /api/competitions/{competitionId}`

## 2. Race Management

### 2.1 Creating a New Race ✅

**Use Case**: An organizer wants to add a new race to a competition.

**Example Data**:

```json
{
  "style": "Butterfly",
  "distance": 200,
  "category": "Senior",
  "gender": "Female",
  "dateTime": "2025-07-16T14:30:00",
  "competitionId": "123e4567-e89b-12d3-a456-426614174000"
}
```

**Expected Endpoint**: `POST /api/competitions/{competitionId}/races`

### 2.2 Viewing All Races in a Competition ✅

**Use Case**: A user wants to list all races scheduled for a competition.

**Expected Endpoint**: `GET /api/competitions/{competitionId}/races`

### 2.3 Updating Race Information ✅

**Use Case**: An organizer needs to change the time or details of a race.

**Example Data**:

```json
{
  "dateTime": "2025-07-16T16:00:00"
}
```

**Expected Endpoint**: `PUT /api/races/{raceId}`

## 3. Competitor Management

### 3.1 Registering a New Competitor ✅

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

### 3.2 Viewing Competitor Details ✅

**Use Case**: A user wants to see detailed information about a competitor.

**Expected Endpoint**: `GET /api/competitors/{competitorId}`

### 3.3 Updating Competitor Information ✅

**Use Case**: A competitor needs to update their personal data.

**Example Data**:

```json
{
  "club": "Elite Swimmers"
}
```

**Expected Endpoint**: `PUT /api/competitors/{competitorId}`

## 4. Result Management

### 4.1 Recording a Race Result ✅

**Use Case**: After a race, an official needs to record a competitor's result.

**Example Data**:

```json
{
  "competitorId": "789e4567-e89b-12d3-a456-426614174001",
  "time": "00:02:15.430"
}
```

**Expected Endpoint**: `POST /api/races/{raceId}/results`

### 4.2 Viewing Results for a Race ✅

**Use Case**: A user wants to see all results from a specific race.

**Expected Endpoint**: `GET /api/races/{raceId}/results`

### 4.3 Viewing Results for a Competitor ✅

**Use Case**: A user wants to see all results achieved by a competitor.

**Expected Endpoint**: `GET /api/competitors/{competitorId}/results`

## 5. Advanced Features

### 5.1 Race Standings ❌

**Use Case**: A user wants to view the current standings for a race.

**Expected Endpoint**: `GET /api/races/{raceId}/standings`

### 5.2 Competition Medal Table ❌

**Use Case**: A user wants to view the medal table for a competition.

**Expected Endpoint**: `GET /api/competitions/{competitionId}/medal-table`

### 5.3 Personal Best Times ❌

**Use Case**: A competitor wants to see their personal best times.

**Expected Endpoint**: `GET /api/competitors/{competitorId}/personal-bests`
