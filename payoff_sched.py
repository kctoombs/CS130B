#!/usr/bin/env python3                                                                     
import sys


#Finds the latest job that doesn't overlap with job start_index                            
def latestNonOverlappingJob(jobList, start_index):
    Min = 0
    firstJob = start_index - 1

    #binary search                                                                         
    while Min <= firstJob:
        mid = (Min + firstJob) // 2
        if jobList[mid][1] <= jobList[start_index][0]:
            if jobList[mid + 1][1] <= jobList[start_index][0]:
                Min = mid + 1
            else:
                return mid
        else:
            firstJob = mid - 1
    return -1


jobList = [] #Contains the inputs                                                          
optList = [] #Contains the optimal intervals. This is a list of lists                      
profits = [] #Contains the optimal profit                                                  
optIndexes = []
jobList.append((0, 0, 0))

#Read from stdin and append each job to the jobList                                        
for line in sys.stdin:
    current = line.split()
    start = int(current[0])
    finish = int(current[1])
    profit = int(current[2])
    triple = (start, finish, profit)
    jobList.append(triple)


#Sort by finish times                                                                      
jobList.sort(key = lambda x: x[1], reverse = False)

#Set the first element of the profit array and the optimal array                           
#profits.append(jobList[0].profit)                                                         
profits.append(jobList[0][2])
optList.insert(0, [jobList[0]]);
optIndexes.append(0)

listLength = len(jobList)

for i in range(1, listLength):
    currentProfit = jobList[i][2]

    nextJobIndex = int(latestNonOverlappingJob(jobList, i))
    
    if(nextJobIndex != -1):
        currentProfit = currentProfit + profits[nextJobIndex]

        #print "currentProfit in if %i" %(currentProfit)                                   

    maxProf = max(currentProfit, profits[i-1])

    #print "maxProf %i" %(maxProf)                                                         
    profits.append(maxProf)

    #This means that the current job has the greatest profit so far                        
    #and that it overlaps with all previous jobs                                           
    if(maxProf == currentProfit and nextJobIndex == -1):
        optIndexes.append(0)


    #This means that the current job plus some other job(s) have the                       
    #greatest profit so far, so add them all                                               
    elif(maxProf == currentProfit and nextJobIndex != -1):
        optIndexes.append(nextJobIndex)

    #This means that the current job would not optimize the profit                         
    #So just append the negative value of the last index                                   
    elif(maxProf == profits[i-1]):
        optIndexes.append(-abs(i - 1))

intervals = []
optLength = len(optIndexes) - 1
lastIndex = optIndexes[-1]
#Iterates in reverse fashion through optIndexes until it finds the last job that was used  

while(optLength != 0):
    if(optIndexes[optLength] < 0):
        optIndexes[optLength] = abs(optIndexes[optLength])
        optLength = optIndexes[optLength]

    else:
        intervals.append(jobList[optLength])
        optLength = optIndexes[optLength]


print("Max Payoff: %i" %(profits[-1]))
l = len(intervals) - 1
while(l != -1):
    print("%i %i %i" %(intervals[l][0], intervals[l][1], intervals[l][2]))
    l -= 1

