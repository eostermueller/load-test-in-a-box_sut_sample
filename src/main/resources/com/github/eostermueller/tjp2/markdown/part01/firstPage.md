[meta]: # (sortOrder=1)
[meta]: # (displayName=Problem #1 - Chunky vs. Chatty)
# Chunky vs. Chatty

After a week or so of coding into the wee hours of the night, Onias was exhausted but finally ready to share the results of his top-secret project.  But he gasped out loud and his decaf latte spilled into his lap when his boss broke the surprise to him.  Two of his fellow developers had been given the **exact** same secret assignment: to code a web service that retrieved balances from 10 different accounts.  If he'd known it was a performance contest, perhaps he would have done everything differently.

All three developers' code would now be benchmarked to see whether any of them had discovered and employed an approach that materially boosted performance.  They all had diligently coded to the same specifications that required a specific json REST API format and was backed by the same data with the same db schema.

As Onias cleaned up the mess on his shorts, he angsted about a key design choice that he'd made.  He had considered these two approaches and really hoped he made the right choice:
* Executing many fast SQL queries with smaller result sets ('Chatty') 
* Executing fewer SQL queries with larger result sets with slower response times ('Chunky').

Before launching and exploring the performance of these 3 workloads, venture a guess at these questions:  
1. Will a Chatty approach help or hurt performance, or not impact it at all?  
1. Where is the majority of time spent in the approach you feel is slower?

To begin, apply load to the system by making sure there is a check in the checkbox next to 'Load Generator', above.
Then:

1. Click on one of the developer names below to launch their Java code.
1. Use the glowroot monitoring link at the very bottom of the page to review performance.  Spend a few minutes looking at each workload's performance.
1. Repeat for each workload.

...and then answer the questions below.

!{yoyong}

!{yolanda}

!{onias}

# Questions

1. Yoyong's queries to the ACCOUNTS table had a single row result set.  What was this query's average response time?
1. What was the response time of Yolanda's query to the ACCOUNTS table, where 10 different accounts were selected?
1. Compare the results for Yoyong's and Yolanda's code.  For a single round trip to this Java service, how much time is spent (on average) querying the ACCOUNTS table?
1. Detail which design option (Chunky, Chatty or neither) each developer embraced.
1. Did you guess correctly whether Chunky or Chatty boosted performance?
