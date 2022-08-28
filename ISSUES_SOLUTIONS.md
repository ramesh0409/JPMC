## Issues Identified
* I didn't find a valid use case for the LocalDateProvider singleton
    * If the intention is to create a single instance of LocalDate.now() then it is invalid . LocalDate is immutable hence every time when you call LocalDate.now() it will create a new instance
    * I have added test case at LocalDateProvider to prove the above point.
    * I have converted the singleton class into an Util class with static methods.
* Business Logic in Data Model classes.
    * I have created business logic classes for each data model and moved the business logic to the respective classes
    * Show ==> ShowManager
    * Movie ==> MovieManager
    * Reservation ==> ReservationManager
    * All Discount and pricing logic moved to PricingService
* Reservation feature was incomplete
    * I have rewritten the reservation feature. It checks the number of tickets against the theatre capacity and reserve accordingly.
* Movie data model had commented else logic. 
* Data model Design issues
    * Reservation object had entire Customer and Show Objects.
    * I have modified that into just customerId and ShowId . Reservation manager will do the linking while reserving a ticket
    * All data model I have redesigned now it has simple POJO No Business Logic
* Exception handling
    * Created custom Runtime Exceptions.
## Test case Issues
* Poor test case coverage
* Few test cases did not assert the results. Just system out was there
* I have added a TestBase class to have a common test utils and test entity.
* Added test case for all scenarios. Test coverage in my Intellij I can see 94%
## New Features
* Completed all the requested new requirements
    * 11 AM to 4PM 25% , 
    * Show 7 $1 
    * Max discount should be taken
* Provided a feature to add and remove new discount rules without modifying the code.
    * PricingService has a method to add and remove new discount rules on the fly.
*By using TestBase class created a base for Test Framework we can further enhance this to reduce lots of coding in Test cases
## TODO
* Include Spring dependency
* Make all classes as Spring component
* In the current code I have not written any singleton classes. 
* Instead of that I want to use Spring bean and convert all Business logic classes as Spring Singleton Beans
* Payment service and update the Reservation paid field

