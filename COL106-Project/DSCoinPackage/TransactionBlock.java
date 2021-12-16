package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    Transaction[] Arr = new Transaction[t.length];
    for (int i=0;i< t.length; i++){
      Arr[i]=t[i];
    }
    this.trarray=Arr;
    this.previous=null;
    MerkleTree helper=new MerkleTree();
    helper.Build(trarray);
    this.Tree=helper;
    this.dgst=null;
    this.nonce=null;
    this.trsummary= this.Tree.rootnode.val;
    
  }

  public boolean checkTransaction (Transaction t) {
    if (t.coinsrc_block==null){
      return true;
    }
    TransactionBlock tb = this;
    while(tb != t.coinsrc_block){

      Transaction[] trs = tb.trarray;
      for(Transaction i : trs){
        if(i.coinID.equals(t.coinID)){
          return false;
        }
      }

      tb = tb.previous;
    }

    for(Transaction i : tb.trarray){
      if(i.coinID.equals(t.coinID)){
        if(i.Destination == t.Source){
          return true;
        }
      }
    }
    return false;
    // for (int i=0; i<trs.length; i++) {
    //   if (trs[i].coinID.equals(t.coinID)){
    //     if (t.Source == trs[i].Destination){
    //       TransactionBlock curr= this;
    //       while(curr!=t.coinsrc_block){
    //         for (int j=0;j< curr.trarray.length;j++){
    //           if(curr.trarray[j].coinID.equals(t.coinID)){
    //             return false;
    //           }
    //         }
    //         curr=curr.previous;
    //       }
    //       return true;
    //     }
    //   }
    // }
    // return false;
    
  }
}