# DS-Coin

### Overview:
This is a crytocurrency project, aimed at building a new currency, having its own Collision Resistant Hash Function, Moderators,Miners and Members.
Initially, all the new members are given a set of coins, each coin having its unique coinID. (Can be easily  modified for the real-world case of a member/user purchasing a coin). The members(as the user) can pull transactions and send a coin to other member/user. 
The transaction details, coinID and its history are authenticated by the Miner. Upon having a set of particular number of transactions, the Miner updates the BlockChain by adding a new TransactionBlock. 
The miner also handles the case if there is(are) any pre-existing Malicious TransactionBlock(s) in the BlockChain. If so, the Miner simply adds to the longest Honest SideChain of the mainBlockChain.

#### For more details Please see the Project Description and Examples.
or contact me at hwadhwa.iitd@gmail.com
