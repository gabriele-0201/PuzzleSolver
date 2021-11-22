# Pseudo code

Input of tha string board
Create first node
Add it to he priority queue
loop until find the end:
    thisNode <- priorityQueue.remvoeMin()

    sons <- thisNode.getSons()

    for son in sons:

        node <- son

        if node = end:
            stop
        
        priorityQueue.add(node)

// node now contain the end
boards <- node.getPrevious()

boards.reverse()

len(boards) // num of moves

boards //array with all the boards