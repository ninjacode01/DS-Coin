package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;
  CRF obj = new CRF(64);

  
  private String FindNonce(TransactionBlock block) {
    String f = block.trsummary;
    String m;
        if (lastBlock==null){
          m= start_string;
        }
        else{
          m= lastBlock.dgst;
        }
    
    
    
    long st =1000000001L;
    String s;
    while(st<=9999999999L){
      s= String.valueOf(st);
      if ((obj.Fn(m+"#"+f+"#"+s).substring(0,4)).equals("0000")){
        return s;
      }
      st++;

    }
    
    /*for (int i=0; i<9; i++) {
      int j=1;
      while(j<=999999999){
        String s1 = Integer.toString(j);
        int k = s1.length();
        String chck = s[i].substring(0,10-k)+s1;
        
        String ANS = obj.Fn(m+"#"+f+"#"+chck);
        if (ANS.substring(0,4)=="0000"){
          return ANS;
        }
        j++;
      }

    }*/
    return null;
  }
  public void InsertBlock_Honest (TransactionBlock newBlock) {
    
      newBlock.nonce= FindNonce(newBlock);
      
    if (this.lastBlock==null){
      
      newBlock.dgst= obj.Fn(start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
      lastBlock = newBlock;
      newBlock.previous=null;

    }
    else{
      
      newBlock.dgst= obj.Fn(lastBlock.dgst+"#"+newBlock.trsummary+"#"+newBlock.nonce);
      newBlock.previous=lastBlock;
      lastBlock= newBlock;
    }

  }
}
