Assumptions and things to mention

Mention: 
Not added validations in records body itself because same record(s) are being used for both request and response data trasfer.
This is done to save time by avoiding creation of separate records for request and response data transfer.
So validations are placed in actions.

Assumption:
Users can like/dislike their own posts and comments.
Same user can't both like and dislike a Post or Comment.
Each microservice in the whole system is responsible for it's own authentication mechanism.
Load balancing, routing, rate-limiting, resiliency etc are managed at gateway.

Mention:
Batch processing for likes/dislikes.
If we make an API call for [Every like/remove-like or dislike/remove-dislike operation], we are prone to attract higher traffic.
So, we can convert it to batch operations.
There can be another small microservice sitting behind the gateway, caching all these calls (using redis with option of data persistence (not only runtime)), and ask for batch-process them at once on fixed short-intervals.
(Needless to say, we can add resiliency there)

Mention:
To save time, same TO objects are used for different operations.
This isn't ideal as it may create confusion on user's end by showing non-required fields.
> Swagger annotations can be used and we can show sample request to overcome this situation.

Mention:
Design choices
For DTOs, can use base class and then extend them as per different needs.
Like for coment, there can be a base class comment, and inhertors RootComment, BaseComment, ReplyComment with default enum value in each to reflect the same.
For Notification service, can use patterns like pub-sub, factory, template and can use rate-limiting and template.
Interfaces, Abstract imlementations, concrete implementations can be used.

Mention:
Can also maintain mandatory dependencies in bom file.

Mention:
Can refactor error handling in much better way by using 1 CustomRuntimeException with appropriate mandatory and optional fields, and methods to define certain aspects.
Then handling happens at one place accordingly.

Mention:
DB choices
Comments can have replies (upto any level of nesting (reply on a reply)).
Because of this kind of connections between comments, a graph db suits best here.
I chose Neo4j initially for storing comments with their relations with other comments.
Couldn't make the aplication work as I got stuck at a bug (yes it's kind-of a bug (enhancement required) related to TransactionManager, have discussed with spring-data team itself in VMware).
I switched to use postgres for comments with self-join relation among the comments.
All other data like user, post etc was already in postgres.

Mention:
DB Management:
Can use indexing based on queries as well as partitioning (if applicable).
Can use profiles, and config based on each profile, thus having option to cocfigure separate db connection and other config for different environments.
Can use techniques like partitioning, sharding, replication (read and write instances).
Can use caching.
Can use external configuration properties using spring-cloud-config or we can use common and service wise config maps (if we're using kubernetes).
