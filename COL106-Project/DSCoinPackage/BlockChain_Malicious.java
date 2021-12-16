package DSCoinPackage;

import HelperClasses.CRF;
import HelperClasses.*;


public class BlockChain_Malicious {
  

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    CRF objMal= new CRF(64);
    for (int i=0;i<tB.trarray.length;i++){
      if (tB.previous != null && !tB.previous.checkTransaction(tB.trarray[i])){
        return false;
      }
    }
    String lol;
    if(tB.previous==null){
      lol=start_string;
    }
    else{
      lol=tB.previous.dgst;
    }
    String calc_dgst= objMal.Fn(lol+"#"+tB.trsummary+"#"+tB.nonce);
    if (!calc_dgst.equals(tB.dgst) || !tB.dgst.substring(0,4).equals("0000")){
      return false;

    }
    MerkleTree myTree = new MerkleTree();
    String calc_summary;
    calc_summary = myTree.Build(tB.trarray);
    if (!tB.trsummary.equals(calc_summary)){
      return false;
    }
    return true;
  }

  private int FindMax(int[] arr){
    int i =0;
    int maxx=arr[0];
    for(int x=0;x<arr.length;x++){
      if (maxx < arr[x]){
        maxx= arr[x];        
        i=x;
      }
    }
    return i;
  }

  public TransactionBlock FindLongestValidChain () {
    int n = this.lastBlocksList.length;
    int[] l = new int[n];
    TransactionBlock[] l2 = new TransactionBlock[n];
    for (int i=0; i<n; i++) {
      TransactionBlock block_mal = this.lastBlocksList[i];
      if(block_mal == null){
        break;
      }
      int leng = 0;
      TransactionBlock temp = block_mal;
      while(block_mal!=null){
        if (checkTransactionBlock(block_mal)){
          leng++;
        }
        else{
          leng = 0;
          temp = block_mal.previous;
        }
        block_mal=block_mal.previous;
      }
      l[i]=leng;
      l2[i] = temp;
    }
    int index= this.FindMax(l);
    return l2[index];


    // List<Integer> l = new ArrayList<Integer>(); 
    // List<TransactionBlock> t = new ArrayList<TransactionBlock>();
    // for (int i =0;i<lastBlocksList.length;i++) {
    //   Pair<Boolean,Integer> pair= this.isValid(lastBlocksList[i]);
    //   if(pair.get_first()){
    //     l.add(pair.get_second());
    //     t.add(lastBlocksList[i]);
    //   }
    // }
    // int index= this.FindMax(l);

    // return t.get(index);
  }



  

//isNonceValid and FindNonce are very much copied from BlockChain_Honest.java//
//////////////////////////////////////////////////////////////////////////////

  // private boolean isNonceValid(String s, TransactionBlock last, TransactionBlock block){
  //   CRF objMal2= new CRF(64);
    
  //   if (last.previous==null){
  //     last.dgst = start_string;
  //   }

  //   String p = objMal2.Fn(last.dgst+"#"+block.trsummary+"#"+s);
  //   if (p.substring(0,4).equals("0000")){
  //     return true;
  //   }
  //   return false;
  // }


  private String FindNonce(TransactionBlock block, TransactionBlock last){
    CRF Malobj = new CRF(64);
    String f = block.trsummary;
    String m;
    if (last==null){
      m= start_string;
    }
    else{
      m=last.dgst;
    }
    long st =1000000001L;
    String s;
    while(st<=9999999999L){
      s= String.valueOf(st);
      if ((Malobj.Fn(m+"#"+f+"#"+s).substring(0,4)).equals("0000")){
        return s;
      }
      st++;

    }
    return null;
  }


  // private String FindNonce(TransactionBlock block, TransactionBlock last) {
  //   System.out.println("Finding Nonce.....in Malicious");
  //   String[] s = {"1000000000","2000000000","3000000000","4000000000","5000000000",
  //   "6000000000","7000000000","8000000000","9000000000"};
  //   for (int i=0; i<s.length; i++) {
  //     int j=1;
  //     while(j<1000000000){
  //       String s1 = Integer.toString(j);
  //       int k = s1.length();
  //       k=10-k;

  //       s[i]= ((s[i].substring(0,k)).concat(s1));
        
  //       if (isNonceValid(s[i],last,block)) {
  //         return s[i];
  //       }
  //       j++;
  //     }

  //   }
  //   return null;
  // }



/////////////////////////////////////////////////////////////////
  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock tb ;
    if (lastBlocksList[0]==null){
      tb=null;
    }
    else{
      tb=this.FindLongestValidChain();
    }
    CRF objMal3= new CRF(64);
    
    newBlock.nonce= this.FindNonce(newBlock,tb);
    
    if (tb!=null){
    newBlock.dgst= objMal3.Fn(tb.dgst+"#"+newBlock.trsummary+"#"+newBlock.nonce);}
    else{
      newBlock.dgst= objMal3.Fn(start_string+"#"+newBlock.trsummary+"#"+newBlock.nonce);
    }
    newBlock.previous=tb;
    int idx = 0;
    for(int i=0;i<lastBlocksList.length;i++){
      if (tb==lastBlocksList[i]){
        lastBlocksList[i]=newBlock;
        return;
      }
      if(lastBlocksList[i] == null){
        idx = i;
        break;
      }
    }
    lastBlocksList[idx] = newBlock;
    return;
  }
}
