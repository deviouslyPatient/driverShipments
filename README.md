### This is a code test assignment for Platform Science completed by Mary Maguire
The application reads in a list of drivers and a list of shipments and uses a proprietary algorithm to compute the best assignments. Results are displayed in a list for users to view.

#### Questions I would want to ask about product requirements:
- Request app UI mock ups or a design review to make sure that the list of drivers and the display the assigned shipments is acceptable
- Verify that a "Y" counts as a vowel in the evaluation algorithm
- Will our evaluation ever have to consider names that use non English characters or symbols?
- Do we need to consider trimming white space off of input?
- Is it possible to have more than one driver with the same name
- Is it possible to have more than one shipment with the same destination
- The instructions say that we get this data every day, I'd be curious about what that means for policies around storing and refreshing data. Will the data will be provided by a network call eventually? New json files every day seems unlikely.


#### Thoughts while working through the code test
- Was the inclusion of the json data as an image instead of text intentional? If an interviewee mistranslates that data it will result in incorrect suitability scores.
- I'm a bit surprised by the request to post this to a public repository, it means you could search through GitHub or other sites and find the solutions other people have completed. In past work places, we have requested just an exported zip from a private repository then one of our devs would set the project back up in our own internal GitHub so multiple developers could still view the code and the commit history.
- The amount of computation necessary for this problem is overly high for an Android app. If this came up in real life I would suggest that the computation happen on a much more robust computer with the results provided to the app via some endpoint.