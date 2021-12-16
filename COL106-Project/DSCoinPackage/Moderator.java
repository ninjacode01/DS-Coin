package DSCoinPackage;
import HelperClasses.Pair;
public class Moderator
 {
////////// Exception Added /////////
  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    Members Moderator = new Members();
    Moderator.UID= "Moderator";
    DSObj.latestCoinID="99999";
    int numMembers= DSObj.memberlist.length;
    int i=0;
    while(coinCount>0){
      while(i<numMembers){
        if(coinCount<0){
          break;
        }
        Members M = DSObj.memberlist[i];
        Transaction tx = new Transaction();
        tx.Source=Moderator;
        tx.Destination=M;
        tx.coinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID)+1);
        tx.next=null;
        tx.coinsrc_block= null;
        DSObj.latestCoinID=tx.coinID;
        DSObj.pendingTransactions.AddTransactions(tx);
        i++;
        coinCount--;
      }
      
      i=0;
      
    }
    while(DSObj.pendingTransactions.size()>0){
      Transaction[] THANKUMODIJI = new Transaction[DSObj.bChain.tr_count];
      try{
      for (int mod_it=0; mod_it< DSObj.bChain.tr_count;mod_it++){
        Transaction mod =DSObj.pendingTransactions.RemoveTransaction();
        
        THANKUMODIJI[mod_it]=mod;
      }
    }
      catch(EmptyQueueException e){}
      TransactionBlock newmodBlock= new TransactionBlock(THANKUMODIJI);

      DSObj.bChain.InsertBlock_Honest(newmodBlock);
      for (Transaction trans : DSObj.bChain.lastBlock.trarray){
        trans.Destination.mycoins.add(new Pair<String,TransactionBlock>(trans.coinID,DSObj.bChain.lastBlock));
      }

    }
    




  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
    

    Members Moderator = new Members();
    Moderator.UID= "Moderator";
    DSObj.latestCoinID="99999";
    int numMembers= DSObj.memberlist.length;
    int i=0;
    while(coinCount>0){
      while(i<numMembers){
        if(coinCount == 0){
          break;
        }
        Members M = DSObj.memberlist[i];
        Transaction tx = new Transaction();
        tx.Source=Moderator;
        tx.Destination=M;
        //System.out.println(M.UID);
        tx.coinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID)+1);
        tx.next=null;
        tx.coinsrc_block= null;
        DSObj.latestCoinID=tx.coinID;
        DSObj.pendingTransactions.AddTransactions(tx);
        i++;
        coinCount--;
      }
      
      i=0;
      
    }

    // System.out.println(DSObj.pendingTransactions.size());
    
    try{
    while(DSObj.pendingTransactions.size()>0){
        
        Transaction[] THANKUMODIJI = new Transaction[DSObj.bChain.tr_count];
        for (int mod_it=0; mod_it< DSObj.bChain.tr_count;mod_it++){
        // if (DSObj.pendingTransactions.size()==0){
        //   break;
        // }
        // System.out.println(mod_it);
        Transaction mod =DSObj.pendingTransactions.RemoveTransaction();
        //System.out.println(mod.Destination.UID);
        
        THANKUMODIJI[mod_it]=mod;
      }
      TransactionBlock newmodBlock= new TransactionBlock(THANKUMODIJI);
  
      DSObj.bChain.InsertBlock_Malicious(newmodBlock);
      for (Transaction trans : DSObj.bChain.lastBlocksList[0].trarray){
        trans.Destination.mycoins.add(new Pair<String,TransactionBlock>(trans.coinID,DSObj.bChain.lastBlocksList[0]));
      }
    }
  }
  catch(EmptyQueueException e){
    e.printStackTrace();
  }
    

  }
}
