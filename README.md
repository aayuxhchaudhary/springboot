- **Google `libphonenumber`** (`8.13.55`)



`POST http://localhost:8085/validate`

#### Request Body
```json
{
  "countryCode": "TH",
  "phoneNumber": "81 234 5678"
}
```
> Accepts 2-letter ISO codes (`TH`, `US`, `GB`, `IN`) or numeric dial codes (`+66`, `66`, `+1`, `91`).

#### Success Response (`200 OK`)
```json
{
  "valid": true,
  "message": "Phone number is valid for India"
}
```

#### Error Response (`400 Bad Request`)
```json
{
  "valid": false,
  "message": "Phone number is too short for India (found 5 digits)"
}
```

### Screenshots

#### 1. Proper Validation According to Country Codes
<img width="830" height="570" alt="Validation according to country codes" src="https://github.com/user-attachments/assets/fbe036c5-9a64-4f5b-8d2f-2b15f68ad3c4" />

#### 2. Proper Error Messages
<img width="830" height="570" alt="Proper error messages" src="https://github.com/user-attachments/assets/b9bb1252-dc7b-44d6-a494-07be0524d74e" />

#### 3. Supports Both Dial Code & ISO Country Code
<img width="830" height="570" alt="Supports both dial code and country code" src="https://github.com/user-attachments/assets/6959a8ae-5890-4865-8316-ecd4f6403839" />

#### 4. Proper Success Message
<img width="830" height="570" alt="Proper success message" src="https://github.com/user-attachments/assets/06c56db5-12fe-4f31-8364-0ae1c73be34b" />

#### 5. Empty Input Validation
<img width="830" height="570" alt="image" src="https://github.com/user-attachments/assets/5514fe9b-2c4f-480b-b9ea-b5ddbb6c6978" />

#### 6. Non-Numeric Character Protection
<img width="830" height="570" alt="image" src="https://github.com/user-attachments/assets/b81afeed-12ad-4dcd-9e9f-47021c254361" />

#### 7. Global Country Validation
<img width="830" height="570" alt="image" src="https://github.com/user-attachments/assets/a697cc80-1965-465a-8c08-ea4f93b5f462" />

