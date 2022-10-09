## URL: http://localhost:9999

## Routes

### [POST] /api/v1/auth/register
> roles: (ROLE_ADMIN, ROLE_USER)
```JSON
{
    "firstname": "alex",
    "lastname": "parco",
    "email": "alex@test.com",
    "role": "ROLE_USER",
    "password": "password",
}
```
## Response
    user registered successfully

### [POST] /api/v1/auth/login
```json
{
    "email": "alex",
"password": "password",
}
```
## Response
```json
{

	"id": 1,
	"firstname": "alex",
	"lastname": "parco",
	"email": "alex@test.com",
	"role": "ROLE_USER",
	"avatar": "",
	"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbG9uc29AdGVzdC5jb20iLCJpYXQiOjE2NjUyNzA5MzUsImV4cCI6MTY2NTM1NzMzNX0.1Fr8upVN1ZdYjuPa2V2wEqMUl0D7qvMhwsxHBVXCKBI"
}
```

## Testin jwt Routes

### [GET] /api/v1/test/admin
```json
{
    "header": {
        "Authorization": "bearer ${token}",
    }
}
```
## Response 
    Welcome to admin route

### [GET] /api/v1/test/user
```json
{
    "header": {
        "Authorization": "bearer ${token}",
    }
}
```
## Response 
    Welcome to user route

### [GET] /api/v1/test/test

## Response 
    
    Welcome to test route

