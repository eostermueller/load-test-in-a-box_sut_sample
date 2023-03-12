[meta]: # (sortOrder=1)
[meta]: # (displayName=Problem #1 - Low Earth Orbit)

# Low Earth Orbit

Everyone on the team remembered that one conference call when Pat gravely said "there's a problem' and all the background chatter on the call immediately went silent.  Pat never says "there's a problem" and everyone knew it.  

You see, worklife had been a bit more challenging as of late.  One day their company was deploying small scale video streaming services for corporate events, and now they were about to attempt the first ever live global video broadcast of a professional sporting event that was actually scheduled to take place on a massive space station in low-earth orbit (LEO).  Athlete's competing in zero gravity!  Failing to meet performance expectations on this project could be disastrous for the ambitious little company.

So, the LEO network was promised to have 100ms or faster ping latency, or at least that's what everyone said.  
Pat asked Gomer to run the performance tests on their software in a test environment to make sure it could meet the contract's 1500ms response time expectation with 100ms ping time.  Click on the link below to run the test -- but first make sure the load generator is running -- you do this by making sure the checkbox next to the "Load Generator" is checked above.

!{gomer}

Pat was satisfied with Gomer's results, but could not muster the courage to mention to anyone that neither their CEO, nor anyone else, got it written into the contract that network ping time from Pat's team's software to the space station would be 100ms or less, most (like 99%) of the time.  That said, the one thing that was indeed clearly present in the contract was that their system (a RESET API) would be queried repeatedly by three concurrent threads -- and Gomer's test had that part covered.

The precursor to the "there's a problem" moment came want Pat discovered new ping measurements from the LEO network team showing that ping time from where their software would be deployed to the space station regularly degraded all the way to 1000ms.  How regularly?  It all depended on the ever-changing proximity of satellites carrying the signal.  

Pat asked Bongoyo to re-run Gomer's test, but to increase the simulated time to ping the space station from 100ms to 1000ms. This would vet the worst case ping time scenario.  The results were abysmal.  The entire team went silent when Pat uttered his infamous words, "there's a problem."  Have a look at this test for yourself.  Yes, average response time was slower than the expected 1500ms.  How bad was it?

!{bongoyo}

# First Set of Questions

1. What is the concurrency of Gomer's test?  Of Bongoyo's test?

1. Say 'three thread dumps' 10 times as fast as you can.  Now use jstack (it is a command line program in the 'bin' folder of the jdk) to capture three thread dumps from the system under test in Bongoyo's test.  You will first need to find the process id (aka pid) for the process running the Java class com.github.eostermueller.tjp2.PerformanceSandboxApp.  What single word/indicator in the thread dumps points to the problem?

1. How many times is this word/indicator repeated in each of the thread dumps of Bongoyo's test?  

1. Pat had been happy with Gomer's test, where response time beat the 1500ms expectation.  But in spite of meeting the 1500ms target, were there signs of problems in that test?  Yes or no?  If 'yes', please detail the problem.

1. Both Gomer's and Bongoyo's tests correctly implement the contract's requirement that 3 threads would always be querying the system.  At a single point in time, how many of those 3 threads in Gomer's and Bongoyo's tests were NOT 'labeled' with the word/indicator mentioned above?

# Second Set of Questions

Pat was skeptical when Rita said (with an impish grin) that she made a surprisingly small code change to fix the problem -- and the main thing that fixed it was removing a single Java keyword from the code.   
Rita ran one test with her code herself using 100ms ping time.  Then Katrina re-ran a test with Rita's fix, running the test with 1000ms ping time.
Pat, still skeptical, couldn't help but ask whether a small percentage (perhaps 1 to 5 percent) of the requests in the test had failed.  Rita and Katrina both replied that 100% of all Servlet round trips were successful -- zero errors.

Here are the two tests (100ms and 1000ms ping time, respectively) with Rita's code change:

!{Rita}

!{Katrina}

1. What is the concurrency on these two tests?

1. Did either of these tests meet the 1500ms response time goal?  

1. What was the throughput of the two tests with 100ms ping time to the space station?  (Gomer's test and Rita's test)

1. Was Pat justified in being skeptical?  AKA, had removing a single Java keyword really improved response time?  If removing that keyword was the fix, what piece of evidence in the thread dump proves it?

1. Why did Pat ask about the amount of failed requests in the tests?
