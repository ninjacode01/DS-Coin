package DSCoinPackage;

import java.util.*;
import HelperClasses.Pair;

public class Members {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
    if (in_process_trans == null) {
      in_process_trans = new Transaction[100];
    }

    Pair<String, TransactionBlock> init = mycoins.remove(0);
    String coin = init.get_first();
    TransactionBlock coin_src = init.get_second();
    Transaction tobj = new Transaction();
    for (int i = 0; i < DSobj.memberlist.length; i++) {
      if (DSobj.memberlist[i].UID.equals(destUID)) {
        tobj.Destination = DSobj.memberlist[i];
      }
    }
    tobj.Source = this;
    tobj.coinID = coin;
    tobj.coinsrc_block = coin_src;
    tobj.next = null;

    boolean task = false;
    while (!task) {

      int len = in_process_trans.length;
      if (in_process_trans[len - 1] == null) {
        for (int i = 0; i < in_process_trans.length; i++) {
          if (this.in_process_trans[i] == null) {
            this.in_process_trans[i] = tobj;
            task = true;
            break;
          }
        }
      } else {
        Transaction[] newArr = new Transaction[len + 100];
        newArr[len] = tobj;
        for (int i = 0; i < len; i++) {
          newArr[i] = this.in_process_trans[i];
        }
        this.in_process_trans = newArr;
        task = true;
      }
    }

    DSobj.pendingTransactions.AddTransactions(tobj);

  }

  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
    if (in_process_trans == null) {
      in_process_trans = new Transaction[100];
    }

    Pair<String, TransactionBlock> init = mycoins.remove(0);
    String coin = init.get_first();
    TransactionBlock coin_src = init.get_second();
    Transaction tobj = new Transaction();
    for (int i = 0; i < DSobj.memberlist.length; i++) {
      if (DSobj.memberlist[i].UID.equals(destUID)) {
        tobj.Destination = DSobj.memberlist[i];
      }
    }
    tobj.Source = this;
    tobj.coinID = coin;
    tobj.coinsrc_block = coin_src;
    tobj.next = null;

    boolean task = false;
    while (!task) {

      int len = in_process_trans.length;
      if (in_process_trans[len - 1] == null) {
        for (int i = 0; i < in_process_trans.length; i++) {
          if (this.in_process_trans[i] == null) {
            this.in_process_trans[i] = tobj;
            task = true;
            break;
          }
        }
      } else {
        Transaction[] newArr = new Transaction[len + 100];
        newArr[len] = tobj;
        for (int i = 0; i < len; i++) {
          newArr[i] = this.in_process_trans[i];
        }
        this.in_process_trans = newArr;
        task = true;
      }
    }

    DSobj.pendingTransactions.AddTransactions(tobj);

  }


  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend(Transaction tobj,
      DSCoin_Honest DSObj) throws MissingTransactionException {
    TransactionBlock current = DSObj.bChain.lastBlock;
    boolean task2 = false;
    int INDEX = 0;
    while (current != null && !task2) {
      for (int i = 0; i < current.trarray.length; i++) {
        if (current.trarray[i] == tobj) {
          INDEX = i;
          task2 = true;
          break;
        }
      }
      if (!task2) {
        current = current.previous;
      }
    }
    if (current == null) {
      throw new MissingTransactionException();
    }

    assert current.trarray[INDEX] == tobj;
    //System.out.println(INDEX + 1);
    List<Pair<String, String>> path = current.Tree.QueryTransaction(INDEX);
    TransactionBlock iterate_back = DSObj.bChain.lastBlock;
    List<Pair<String, String>> path2 = new ArrayList<Pair<String, String>>();
    while (iterate_back != current) {
      
      path2.add(0, new Pair<String, String>(iterate_back.dgst,
          iterate_back.previous.dgst + "#" + iterate_back.trsummary + "#" + iterate_back.nonce));
      iterate_back=iterate_back.previous;
    }
    String finaliz;
    if(current.previous==null){
      finaliz= null;
    }
    else{
      finaliz = current.previous.dgst;
    }
    path2.add(0,
        new Pair<String, String>(current.dgst, finaliz+ "#" + current.trsummary + "#" + current.nonce));
    path2.add(0, new Pair<String, String>(finaliz, null));

    Pair<String, TransactionBlock> pair = new Pair<String, TransactionBlock>(tobj.coinID, current);
    this.SortedInsert(pair, tobj.Destination);
    return new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(path, path2);
  }

  private void SortedInsert(Pair<String, TransactionBlock> pair, Members M) {
    int coin = Integer.parseInt(pair.get_first());
    for (int i = 0; i < M.mycoins.size(); i++) {
      int coinCheck = Integer.parseInt(M.mycoins.get(i).get_first());
      if (coin < coinCheck) {
        M.mycoins.add(i, pair);
        break;
      }
    }

  }

  public void MineCoin(DSCoin_Honest DSObj) {
    int numtr = 0;
    Transaction[] tr = new Transaction[DSObj.bChain.tr_count];

    HashMap<String, Transaction> map = new HashMap<String, Transaction>();
    while (numtr < DSObj.bChain.tr_count - 1) {
      try {
        Transaction p = DSObj.pendingTransactions.RemoveTransaction();

        if (DSObj.bChain.lastBlock.checkTransaction(p)) {
          if (!map.containsKey(p.coinID)) {
            map.put(p.coinID, p);
            tr[numtr] = p;
            numtr++;
          }
        }
      } catch (EmptyQueueException e) {
      }

    }

    ///////// Miner Reward //////////
    Transaction minerReward = new Transaction();
    minerReward.coinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID) + 1);
    minerReward.coinsrc_block = null;
    minerReward.Source = null;
    minerReward.Destination = this;
    minerReward.next = null;

    DSObj.latestCoinID = minerReward.coinID;

    tr[DSObj.bChain.tr_count - 1] = minerReward;

    //////// Adding new Block to BlockChain //////////

    TransactionBlock newblock = new TransactionBlock(tr);
    
    DSObj.bChain.InsertBlock_Honest(newblock);
    

    this.mycoins.add(new Pair<String, TransactionBlock>(minerReward.coinID, newblock));

  }

  public void MineCoin(DSCoin_Malicious DSObj) {
    TransactionBlock bl = DSObj.bChain.FindLongestValidChain();
    int numtr = 0;
    Transaction[] tr = new Transaction[DSObj.bChain.tr_count];

    HashMap<String, Transaction> map = new HashMap<String, Transaction>();
    
    try {
    while (numtr < DSObj.bChain.tr_count - 1) {
        Transaction p = DSObj.pendingTransactions.RemoveTransaction();
        if (bl.checkTransaction(p)) {
         
          if (!map.containsKey(p.coinID)) {
            map.put(p.coinID, p);
            tr[numtr] = p;
            numtr++;
          }
        }
      }
      
    } catch (EmptyQueueException e) {
      
    }

    ///////// Miner Reward //////////
    Transaction minerReward = new Transaction();
    minerReward.coinID = Integer.toString(Integer.parseInt(DSObj.latestCoinID) + 1);
    minerReward.coinsrc_block = null;
    minerReward.Source = null;
    minerReward.Destination = this;
    minerReward.next = null;

    DSObj.latestCoinID = minerReward.coinID;

    tr[DSObj.bChain.tr_count - 1] = minerReward;

    //////// Adding new Block to BlockChain //////////

    TransactionBlock newblock = new TransactionBlock(tr);
    
    DSObj.bChain.InsertBlock_Malicious(newblock);
    

    this.mycoins.add(new Pair<String, TransactionBlock>(minerReward.coinID, newblock));



  }
}
