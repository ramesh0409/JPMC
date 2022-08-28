# Issues Identified
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



# Introduction

This is a poorly written application, and we're expecting the candidate to greatly improve this code base.

## Instructions
* **Consider this to be your project! Feel free to make any changes**
* There are several deliberate design, code quality and test issues in the current code, they should be identified and resolved
* Implement the "New Requirements" below
* Keep it mind that code quality is very important
* Focus on testing, and feel free to bring in any testing strategies/frameworks you'd like to implement
* You're welcome to spend as much time as you like, however, we're expecting that this should take no more than 2 hours

## `movie-theater`

### Current Features
* Customer can make a reservation for the movie
  * And, system can calculate the ticket fee for customer's reservation
* Theater have a following discount rules
  * 20% discount for the special movie
  * $3 discount for the movie showing 1st of the day
  * $2 discount for the movie showing 2nd of the day
* System can display movie schedule with simple text format

## New Requirements
* New discount rules; In addition to current rules
  * Any movies showing starting between 11AM ~ 4pm, you'll get 25% discount
  * Any movies showing on 7th, you'll get 1$ discount
  * The discount amount applied only one if met multiple rules; biggest amount one
* We want to print the movie schedule with simple text & json format
