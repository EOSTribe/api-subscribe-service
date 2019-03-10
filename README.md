# API Subscribe Service

#### Java REST API subscription service designed to automatically manage and issue access tokens for access to EOS API services.

### About

This service supports 2 POST endpoints: subscribe and renew:

#### /subscribe 

Service has 3 subscription packaged defined: L1, L2 and L3 - can me extended/changed.

Before calling subscription service the user needs to send EOS payment indicating chosen subscription package in memo:

> cleos transfer [account] eostribeapis "50.0000 EOS" "L1" 

After transaction is confirmed on the network - user needs to send a POST request to:

> curl -d '{ "account": "[account]", "transaction": "57db1a072b1786087cb16e916791ac1f8cf586d964e85b4b192e847896a9998d", "secret":"[password]"}' -X POST https://api.eostribe.io/subscribe

The server will validate transaction, take account and password hash and sign the result with the server private key, and return back token:
```
{
    "token": "SIG_K1_JyeT4u3JQa1DQRiumE7PMmorc41y9S8qv3ezg4NpH4RivnsJnZYgsECM54BSUwvBUwcMNGqdTrQjr7E9gWFhpsUd8H3ouK",
    "expiration": "2019-03-15T07:00:00.000+0000",
    "plan": "L1",
    "message": "API subscription key issued and registered."
}
```

Expiration date is calculated based on amount paid and subsciption cost for package selected. 
User can pay prorated payment for a few days or months ahead - expiration will be set accordingly.

Please note that while we use password to calculated signed hash - we are not storing it for security purposes.
Hence user needs to remember his password in order to renew his subscription token.

Calling subscribe endpoint repeatedly with the same request will effectivly work as GET and return the token back.

#### /renew 

User is able to renew his expiring token at any point of time (before or after expiration date) by first sending a new EOS payment.
He can set new subscription package in payment memo to change his subscription as well:

> cleos transfer [account] eostribeapis "150.0000 EOS" "L2" 

After transaction is confirmed - user needs to send POST request to renew with his original [password]:

> curl -d '{ "account": "[account]", "transaction": "57db1a072b1786087cb16e916791ac1f8cf586d964e85b4b192e847896a9998d", "secret":"[password]"}' -X POST https://api.eostribe.io/renew 

The server will validate transaction, find a corresponding account token and update expiration date according to payment amount versus plan cost and return updated token back. If renewal is made before token is expired - the existing expiration date will be extended, otherwise for expired tokens new period started from today.
The signature/token for the same account and password does not change after renewal. 

### How to build this project

> gradle clean build 

### How to run service

You may want to first change global.properties and application.properties to match your environment adn settings. 

Configure local MySQL database for specified user and create SUBSCRIPTIONS table. 

Finally when all settings is done - just run:

> java -jar build/libs/api-subscribe-1.0.0.jar 

### Contact 

Eugene eugene@eostribe.io 





