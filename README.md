# AWSChallenge
amazon challenge for me
________________________________________________
The task consists of calculating summary statistics for a fleet of hosts. Each host has a fixed number of slots in which virtual instances can run. Each host can only run instances of a particular type, e.g. M1, M2, or M3. You are provided with the file “FleetState.txt” that contains the state of the fleet. Each line in the file represents a single host in the following comma-separated format:

\<HostID>,\<InstanceType>,\<N>,\<Slot1State>,\<Slot2State>,…,\<SlotNState>
where
\<HostID> is an integer
\<InstanceType> can be M1, M2, or M3
\<N> is the total number of slots on the machine
\<SlotjState> is 0 if slot j is empty and 1 if it is occupied by an instance
 
Write a program that loads the state of the fleet from the input file “FleetState.txt” and then computes and writes out the following summary statistics to the output file “Statistics.txt”:
 
For each instance type, the count of empty hosts (all slots empty)
For each instance type, the count of full hosts (all slots filled)
For each instance type, the count of the most filled hosts (having the smallest number of empty slots > 0). Both the host count and number of empty slots must be written out in that order.
 
The output file must have the following format:
 
EMPTY: M1=\<count>; M2=\<count>; M3=\<count>;
FULL: M1=\<count>; M2=\<count>; M3=\<count>;
MOST FILLED: M1=\<count>,\<empty slots>; M2=\<count>,\<empty slots>; M3=\<count>,\<empty slots>;
