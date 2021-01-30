# RockPaperScissors

This is a small software to handle the game of RockPaperScissors, it is implemented using a REST and Java 15
divided in 2 different controllers, one for the games (which are treated as individual resources)
and the other for the statistics, since the `GameController` serves the purpose to handle petitions regarding actual
games, that is why another controller is needed to serve the statistics.

This API which has the following calls:

1) Create a new game `POST`
   1.1) Accepts no parameters and returns the game UUID
2) Get game progress based on ID `GET`
   2.1) Accepts the game UUID as a `PathParam` and returns a `List` containing the game progress
3) Restart a game based on ID `PATCH`
   3.1) Same as 2.1) but in this case an empty response is sent.
4) Start a new round based on the ID `PATCH`
   4.1) Same as 2.1) but in this case the information of the newly generated round is sent as a `Map`
5) Retrieved the global statistics about the games played. `GET`
   5.1) Sames as 1) but in this case a `Map` is returned with the statistics data

On a general rule, every petition will return a `SystemResponse` a serializable class used to make
the whole system consistent in terms of always returning the same object hence the same information structure. 

Another detail, the calls 2, 3, and, 4 require the resource ID (game) in order to operate, if the resource is not
found a `HTTP 404` will be returned.

Also, some aforementioned calls are of type `PATCH` since, these calls by definition are not idempotent hence the use of
`PUT` should be avoided. Also, for the call number 3, although it is idempotent we are not "putting" anything in the system
we are still partially modifying the resource that is why the type `PATCH` is applied.

Another matter addressing the use of `PATCH` is that these instructions do not contain any 'configuration', in other words,
they do not receive the a `JSON Patch` document, more info can be found in the RFC6902 https://tools.ietf.org/html/rfc6902
since they are very simple and furthermore, in order to make these changes there are no inputs received from the user.

Also, in order to maintain a memory state of the system, I have implemented a dummy version of `DAO` in order to abstract
the service of the persistence layer.

In this case, no DTO is used since I have not seen any applicable scenario, because these objects are meant to be
as a package in order to send an information compound of different entities in order to save time of making more calls.

