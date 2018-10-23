/**Name: Ori Glassman, ID: 311453427, User Name: origlassman. Name: Itay Levi, ID: 312485386, User Name: itaylevi
 */
/**
*
* WAVLTree
*
* An implementation of a WAVL Tree with
* distinct integer keys and info
*
*/
import java.util.ArrayList;
import java.util.List;


public class WAVLTree_origlassman_itaylevi {

	  private WAVLNode root = null;
	  private WAVLNode minN, maxN;
	  
	  
	  public WAVLTree_origlassman_itaylevi() {//wavl tree constructor
	  }
	  public WAVLTree_origlassman_itaylevi(WAVLNode root) {  
		  this.root=root;
		  this.minN=this.root;
		  this.maxN=this.root;
	  }

	  
 /**
  * public boolean empty()
  *
  * returns true if and only if the tree is empty
  *
  */
	  
 public boolean empty() {
	  if (this.getRoot()==null)
		  return true;
	  else
		  return false;
 }

/**
  * public String search(int k)
  *
  * returns the info of an item with key k if it exists in the tree
  * otherwise, returns null
  */

 public String search(int k)
 {
	  if (this.root==null) {
		  return null;
	  }
	  IWAVLNode node = this.getRoot(); 
	  int nodeKey = node.getKey();
	  while (true) {
		  if (!node.isRealNode())
			  return null;
		  
		  else if (k==nodeKey) {
			  return node.getValue();
		  }
		  else if (k < nodeKey) {
			  node = node.getLeft();
			  nodeKey = node.getKey();
		  }
		  else {
			  node = node.getRight();
			  nodeKey= node.getKey();
		  }		  
	  }
 }

 /**public IWAVLNode TreePosition(IWAVLNode node, int k)
  * returns 'father to be' node where input node should be inserted **/
 public IWAVLNode TreePosition(IWAVLNode node, int k) {
	  IWAVLNode temp=(WAVLNode)node;
	  WAVLNode fatherOfTemp = (WAVLNode)temp;
	  while (temp.isRealNode()) {
		  if (k==temp.getKey()) {
			  return fatherOfTemp; 
		  }
		  else if (k<temp.getKey()){
			  fatherOfTemp=(WAVLNode)temp;
			  temp = temp.getLeft();
		  }
		  else {
			  fatherOfTemp=(WAVLNode)temp;
			  temp = temp.getRight();
		  }  
	  }
	  WAVLNode wavlTemp = (WAVLNode) temp;
	  	  if (wavlTemp.rank==-1) {
	  		  return fatherOfTemp;
	  	  }
	  return temp;
 }

 
 /**
  * public int insert(int k, String i)
  *
  * inserts an item with key k and info i to the WAVL tree.
  * the tree must remain valid (keep its invariants).
  * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
  * returns -1 if an item with key k already exists in the tree.
  */
  public int insert(int k, String i) {
	   if (this.root==null) {	   
		   this.root=new WAVLNode(k,i);
		   this.root.rank=0;
		   this.minN=(WAVLNode) this.getRoot();
		   this.maxN=(WAVLNode) this.getRoot();
		   return 0;
	   }
	   int numOfRebalance =0;
	   WAVLNode insertPoint =(WAVLNode) TreePosition(this.getRoot(), k);//newNode father to be
	   WAVLNode newNode = new WAVLNode(k,i);
	   WAVLNode leftNode = (WAVLNode) insertPoint.getLeft(); WAVLNode rightNode = (WAVLNode) insertPoint.getRight(); //left and right children of the insertion point	   
	   if(insertPoint.getKey()==k) {//key exists in tree		   
		   return -1;
	   }
	   if (k<this.minN.getKey()) {//update the min Node
		   this.minN= newNode;
	   }
	   else if (k>this.maxN.getKey()) {//update the max node
		   this.maxN=newNode;
	   }
	   newNode.setRank(0);//set new nodes rank as 0
	   newNode.setSubtreeSize(0);//set new nodes subTree size as 0
	   if (!leftNode.getLeft().isRealNode() && !rightNode.isRealNode()) {// CASE A, insertPoint is a leaf		   
		   if (k<insertPoint.getKey()) {
			   insertPoint.setLeftNode(newNode);
		   }
		   else {
			   insertPoint.setRightNode(newNode);
		   }
	   }
	   else  { // case B
		   if (insertPoint.getLeft().isRealNode()) { 
			   insertPoint.setRightNode(newNode);
		   }
		   else {
			   insertPoint.setLeftNode(newNode);
		   }
	   }
	   subTreeSizeUpdate(this.getRoot(), insertPoint , 1);//update all nodes subTreeSize in path from newNodes parent to the root; 
	   
	   //rebalance after insertion stage:
	   leftNode = (WAVLNode) insertPoint.getLeft(); rightNode = (WAVLNode) insertPoint.getRight();
	   boolean isBalanced=true;
	   if(insertPoint.getRank()-leftNode.getRank()<1 || insertPoint.getRank()-leftNode.getRank()>2 || 
			   insertPoint.getRank()-rightNode.getRank()<1 || insertPoint.getRank()-rightNode.getRank()>2) {
		   isBalanced=false;
	   }  
	   while (!isBalanced) { 
		   numOfRebalance+=rebalanceAfterInsertion(insertPoint);
		   insertPoint = (WAVLNode) TreePosition(this.getRoot(), insertPoint.getKey());
		   leftNode = (WAVLNode) insertPoint.getLeft(); rightNode = (WAVLNode) insertPoint.getRight();
		   isBalanced=true;
		   if(insertPoint.getRank()-leftNode.getRank()<1 || insertPoint.getRank()-leftNode.getRank()>2 || 
				   insertPoint.getRank()-rightNode.getRank()<1 || insertPoint.getRank()-rightNode.getRank()>2) {
			   isBalanced=false;
		   }
	   }
		   return numOfRebalance;
  }

  
 /**private int rebalanceAfterInsertion(WAVLNode insertPoint)
  * rebalance steps after insertion at current position - insertion point**/
  private int rebalanceAfterInsertion(WAVLNode insertPoint) {
	  WAVLNode leftNode = (WAVLNode) insertPoint.getLeft(); WAVLNode rightNode = (WAVLNode) insertPoint.getRight(); //left and right children of the insertion point
	  int numOfRebalance=0;
	  
	//case2 + case3
	   if (insertPoint.getRank()-leftNode.getRank()==2 && insertPoint.getRank() - rightNode.getRank()==0) { // (2,0)
		   WAVLNode rightOfRight= (WAVLNode) rightNode.getRight();
		   WAVLNode leftOfRight= (WAVLNode) rightNode.getLeft();
		 //case3
		   WAVLNode parentOfInsertPoint = (WAVLNode) TreePosition(this.getRoot(), insertPoint.getKey()); 			   
		   if (rightNode.getRank()-rightOfRight.getRank()==2 && rightNode.getRank()-leftOfRight.getRank()==1) 
		   { 
			   rightNode.setRank(rightNode.getRank()-1);//demote x
			   leftOfRight.setRank(leftOfRight.getRank()+1);//promote b
			   insertPoint.setRightNode(rightRotate(new WAVLTree_origlassman_itaylevi((WAVLNode)insertPoint.getRight())));
			   if(insertPoint.getKey()==this.getRoot().getKey()) {//incase parent is the root
				   this.root= leftRotate(new WAVLTree_origlassman_itaylevi(insertPoint));
			   }
			   else if (parentOfInsertPoint.getKey()<insertPoint.getKey()) {
				   parentOfInsertPoint.setRightNode(leftRotate(new WAVLTree_origlassman_itaylevi(insertPoint))); 
			   }
			   else {
				   parentOfInsertPoint.setLeftNode(leftRotate(new WAVLTree_origlassman_itaylevi(insertPoint)));
			   }
			   insertPoint.setRank(insertPoint.getRank()-1);////demote z
			   return numOfRebalance =  numOfRebalance + 5;
		   }
		 //case2
		   else if (rightNode.getRank()-rightOfRight.getRank()==1 && rightNode.getRank()-leftOfRight.getRank()==2) {
			   if(insertPoint.getKey()==this.getRoot().getKey()) {//incase parent is the root
				  this.root= leftRotate(new WAVLTree_origlassman_itaylevi(insertPoint));
				  insertPoint.setRank(insertPoint.getRank()-1);//demote z
			   }
			   else if (parentOfInsertPoint.getKey()<insertPoint.getKey()){//parent key is smaller then insert point key
				   parentOfInsertPoint.setRightNode(leftRotate(new WAVLTree_origlassman_itaylevi(insertPoint)));	
				   insertPoint.setRank(insertPoint.getRank()-1);//demote z
			   }
			   else {//parent key is bigger then insert point key
				   parentOfInsertPoint.setLeftNode(leftRotate(new WAVLTree_origlassman_itaylevi(insertPoint)));	
				   insertPoint.setRank(insertPoint.getRank()-1);//demote z
			   }
			   return numOfRebalance=numOfRebalance +2;
		   }		   
	   }
	   else if (insertPoint.getRank()-leftNode.getRank()==0 && insertPoint.getRank() - rightNode.getRank()==2) { //(0,2)
		   WAVLNode rightOfLeft= (WAVLNode) leftNode.getRight();
		   WAVLNode leftOfLeft= (WAVLNode) leftNode.getLeft();
		 //case3
		   WAVLNode parentOfInsertPoint = (WAVLNode) TreePosition(this.getRoot(), insertPoint.getKey());
		   if (leftNode.getRank()-leftOfLeft.getRank()==1 && leftNode.getRank()-rightOfLeft.getRank()==2) {
			   if(insertPoint.getKey()==this.getRoot().getKey()) {//in-case parent is the root
				   this.root=rightRotate(new WAVLTree_origlassman_itaylevi(insertPoint));
			   }
			   else if (parentOfInsertPoint.getKey()<insertPoint.getKey()) {//parent key is smaller then insert point key
				   parentOfInsertPoint.setRightNode(rightRotate(new WAVLTree_origlassman_itaylevi(insertPoint)));		   
			   }
			   else {//parent key is bigger then insert point key
				   parentOfInsertPoint.setLeftNode(rightRotate(new WAVLTree_origlassman_itaylevi(insertPoint)));
			   }
			   insertPoint.setRank(insertPoint.getRank()-1);//demote z
			   numOfRebalance=numOfRebalance +2;
			   return numOfRebalance;
		   }			   
		 //case2   
		   else if (leftNode.getRank()-rightOfLeft.getRank()==1 && leftNode.getRank()-leftOfLeft.getRank()==2) {  
			   insertPoint.setLeftNode(leftRotate(new WAVLTree_origlassman_itaylevi((WAVLNode)insertPoint.getLeft())));
			   leftNode.setRank(leftNode.getRank()-1);//demote x
			   rightOfLeft.setRank(rightOfLeft.getRank()+1);//promote b
			   if(insertPoint.getKey()==this.getRoot().getKey()) {//incase parent is the root
				   this.root=rightRotate(new WAVLTree_origlassman_itaylevi(insertPoint));
			   }
			   else if(parentOfInsertPoint.getKey()<insertPoint.getKey()) {//parent key is smaller then insert point key
				   parentOfInsertPoint.setRightNode(rightRotate(new WAVLTree_origlassman_itaylevi(insertPoint))); 
			   }
			   else {//parent key is bigger then insert point key
				   parentOfInsertPoint.setLeftNode(rightRotate(new WAVLTree_origlassman_itaylevi(insertPoint))); 
			   }
			   insertPoint.setRank(insertPoint.getRank()-1);//demote z	   
			   return numOfRebalance =  numOfRebalance + 5;
		   } 
	   }
	 //not case 2 or 3
	   else {
		   insertPoint.setRank(insertPoint.getRank()+1); // promote z 
		   numOfRebalance++;
	   }
	   return numOfRebalance;
}
  
//*retrieves right rotation of the input tree**/  
public static WAVLNode rightRotate(WAVLTree_origlassman_itaylevi tree) {
	   WAVLNode temp =(WAVLNode) tree.getRoot();
	   tree.root=(WAVLNode)tree.root.getLeft();
	   tree.root.setSubtreeSize(temp.getSubtreeSize());//update subtree size for the new root
	   temp.setLeftNode(tree.getRoot().getRight());
	   temp.setSubtreeSize(temp.getLeft().getSubtreeSize()+temp.getRight().getSubtreeSize()+2); //update subtree size for the old root - now became right child of the root
	   tree.root.setRightNode(temp);
	   return tree.root;
	   
  }

//*retrieves left rotation of the input tree**/
  public static WAVLNode leftRotate(WAVLTree_origlassman_itaylevi tree) {
	   WAVLNode temp =(WAVLNode) tree.getRoot();
	   tree.root=(WAVLNode)tree.root.getRight();
	   tree.root.setSubtreeSize(temp.getSubtreeSize());//update subtree size for the new root
	   temp.setRightNode(tree.getRoot().getLeft());
	   temp.setSubtreeSize(temp.getLeft().getSubtreeSize()+temp.getRight().getSubtreeSize()+2); //update subtree size for the old root - now became the Left child of the root
	   tree.root.setLeftNode(temp);
	   return tree.root;
  }
  

 /**
  * public int delete(int k)
  *
  * deletes an item with key k from the binary tree, if it is there;
  * the tree must remain valid (keep its invariants).
  * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
  * returns -1 if an item with key k was not found in the tree.
  */
  public int delete(int k) {
	   
  if (search(k)==null)//key not found
  {
	   return -1;
  }
  WAVLNode parent= (WAVLNode) TreePosition(this.getRoot(), k);
  WAVLNode rightNode= (WAVLNode) parent.getRight();
  WAVLNode leftNode= (WAVLNode) parent.getLeft();
  int numOfRebalance=0;
  subTreeSizeUpdate(this.getRoot(), parent , -1);//update all nodes subTreeSize in path from newNodes parent to the root
  if (k==parent.getKey()) {//delete root
	   if (rightNode.getRank()==-1 && leftNode.getRank()==-1) {// deleted node is a leaf 
		   this.root=new WAVLNode(-1,null);//deletion
		   if(k==this.getRoot().getKey()) {//update trees min node
				  minN= findMin((WAVLNode) this.getRoot());
			  	}
		   if(k== maxNode(this.root).getKey()) {//update trees max node
				maxN= findMax((WAVLNode) this.getRoot());  
			  	}
		   return numOfRebalance;
	   }
	   else if ( (parent.getRank()-leftNode.getRank()==1 && !rightNode.isRealNode()) ||
			   (parent.getRank()-rightNode.getRank()==1 && !leftNode.isRealNode()) ) {// deleted node is an unary node
		   WAVLNode realChild;//the 'real' child of y
		   if(parent.getLeft().isRealNode()) {
			   realChild= (WAVLNode) parent.getLeft();
		   }
		   else {
			   realChild= (WAVLNode) parent.getRight();
		   }
		   this.root=realChild;//deletion
		   if(k==minNode(this.root).getKey()) {//update trees min node
				  minN= findMin((WAVLNode) this.getRoot());
			  	}
		   if(k== maxNode(this.root).getKey()) {//update trees max node
				maxN= findMax((WAVLNode) this.getRoot());  
			  	}
		   if ( parent.getRank()-leftNode.getRank()!=3 && parent.getRank()-rightNode.getRank()!=3 ) {//if there is no 3 rank difference
			   return numOfRebalance;
		   }
		   //else - this is case (2,1 or 2), now continue to rebalance after deletion  
	   }	   
	   else {// deleted node is a binary node
		   WAVLNode deletedNodeSuccessor=(WAVLNode) successor(parent);//y
		   delete(deletedNodeSuccessor.getKey());//deletion
		   if(k==minNode(this.root).getKey()) {//update trees min node
				  minN= findMin((WAVLNode) this.getRoot());
			  	}
		   if(k== maxNode(this.root).getKey()) {//update trees max node
				maxN= findMax((WAVLNode) this.getRoot());  
			  	}
		   deletedNodeSuccessor.setRank(this.root.getRank());
		   this.root=deletedNodeSuccessor;
		   deletedNodeSuccessor.setRightNode(parent.getRight());// attaching z right sons with y getLeft().getRight()
		   deletedNodeSuccessor.setLeftNode(parent.getLeft());// attaching z left sons with y .getLeft().getLeft()
		   //now continue to rebalance after deletion  	   
	   } 	     
  }
  else if (k<parent.getKey()) {//delete left child
	   WAVLNode rightOfLeft= (WAVLNode) leftNode.getRight();
	   WAVLNode leftOfLeft= (WAVLNode) leftNode.getLeft();
	   if (!rightOfLeft.isRealNode() && !leftOfLeft.isRealNode()) {// deleted node is a leaf 
		   parent.setLeftNode(new WAVLNode(-1,null));//deletion
		   if(k==minN.getKey()) {//update trees min node
				  minN= findMin((WAVLNode) this.getRoot());
			  	}
		   leftNode = (WAVLNode)parent.getLeft(); rightNode = (WAVLNode)parent.getRight();
		   if (parent.getRank()-leftNode.getRank()==2 && parent.getRank()-rightNode.getRank()==1) {//case (2,1) , was (1,1) before
			   return numOfRebalance;
		   }
		   else if ( parent.getRank()-leftNode.getRank()==2 && parent.getRank()-rightNode.getRank()==2) {//case (2,2) , was (1,2) before
			   parent.setRank(parent.getRank()-1);
			   parent= (WAVLNode) TreePosition(this.getRoot(),parent.getKey());
			   numOfRebalance++;//now continue to rebalance after deletion  
		   }
		   //else - this is case (2, 1 or 2), now continue to rebalance after deletion  
	   }
	   else if ( (leftNode.getRank()-leftOfLeft.getRank()==1 && !rightOfLeft.isRealNode()) ||
			   (leftNode.getRank()-rightOfLeft.getRank()==1 && !leftOfLeft.isRealNode()) ) {// deleted node is an unary node
		   WAVLNode realChild;
		   if(leftOfLeft.isRealNode()) {
			   realChild= (WAVLNode) leftOfLeft;
		   }
		   else {
			   realChild= (WAVLNode) rightOfLeft;
		   }
		   parent.setLeftNode(realChild);//deletion
		   if(k==minN.getKey()) {//update trees min node
				  minN= findMin((WAVLNode) this.getRoot());
			  	}
		   rightNode= (WAVLNode) parent.getRight();
		   leftNode= (WAVLNode) parent.getLeft();
		   if ( parent.getRank()-leftNode.getRank()!=3 && parent.getRank()-rightNode.getRank()!=3 ) {//if there is no 3 rank difference
			   return numOfRebalance;
		   }
		   //else - this is case (2,1 or 2), now continue to rebalance after deletion   
	   }	   
	   else if (rightOfLeft.isRealNode() && leftOfLeft.isRealNode()){// deleted node is a binary node
		   WAVLNode deletedNodeSuccessor=(WAVLNode) successor(leftNode);//y (leftNode is Z)
		   delete(deletedNodeSuccessor.getKey());//deletion
		   if(k==minN.getKey()) {//update trees min node
				  minN= findMin((WAVLNode) this.getRoot());
			  	}
		   deletedNodeSuccessor.setRank(leftNode.getRank());
		   deletedNodeSuccessor.setRightNode(leftNode.getRight());// attaching z right sons with y
		   deletedNodeSuccessor.setLeftNode(leftNode.getLeft());// attaching z left sons with y
		   parent.setLeftNode(deletedNodeSuccessor);//attaching z parent to y
		   //now continue to rebalance after deletion  
	   }	   
  }
  else {////delete right child
	   WAVLNode rightOfRight= (WAVLNode) rightNode.getRight();
	   WAVLNode leftOfRight= (WAVLNode) rightNode.getLeft();
	   if (!rightOfRight.isRealNode() && !leftOfRight.isRealNode()) {// deleted node is a leaf 
		   parent.setRightNode(new WAVLNode(-1,null));//deletion
		   if(k== maxN.getKey()) {//update trees max node
				maxN= findMax((WAVLNode) this.getRoot());  
			  	}
		   rightNode=(WAVLNode) parent.getRight();
		   if (parent.getRank()-leftNode.getRank()==1 && parent.getRank()-rightNode.getRank()==2) {//case (1,2) , was (1,1) before
			   return numOfRebalance;
		   }
		   if ( parent.getRank()-rightNode.getRank()==2 && parent.getRank()-leftNode.getRank()==2) {//case (2,2) , was (2,1) before
			   parent.setRank(parent.getRank()-1);
			   parent= (WAVLNode) TreePosition(this.getRoot(),parent.getKey());
			   numOfRebalance++;//now continue to rebalance after deletion  
		   }
		   //else - this is case (1, 2 or 1), now continue to rebalance after deletion  
	   }
	   else if ( (rightNode.getRank()-leftOfRight.getRank()==1 && !rightOfRight.isRealNode()) ||
			   (rightNode.getRank()-rightOfRight.getRank()==1 && !leftOfRight.isRealNode()) ) {// deleted node is an unary node
		   WAVLNode realChild;
		   if(leftOfRight.isRealNode()) {
			   realChild= leftOfRight;
		   }
		   else {
			   realChild= (WAVLNode) rightOfRight;
		   }
		   parent.setRightNode(realChild);//deletion
		   if(k== maxN.getKey()) {//update trees max node
				maxN= findMax((WAVLNode) this.getRoot());  
			  	}
		   rightNode = (WAVLNode)parent.getRight();
		   if ( parent.getRank()-leftNode.getRank()!=3 && parent.getRank()-rightNode.getRank()!=3 ) {//if there is no 3 rank difference
			   return numOfRebalance;
		   }
		   //else - this is case (1,2 or 1), now continue to rebalance after deletion     
	   }	   
	   else if (rightOfRight.isRealNode() && leftOfRight.isRealNode()) {// deleted node is a binary node
		   WAVLNode deletedNodeSuccessor=(WAVLNode) successor(rightNode);//y	   
		   delete(deletedNodeSuccessor.getKey());//deletion
		   if(k== maxN.getKey()) {//update trees max node
				maxN= findMax((WAVLNode) this.getRoot());  
			  	}
		   deletedNodeSuccessor.setRank(rightNode.getRank());
		   deletedNodeSuccessor.setRightNode(rightNode.getRight());// attaching z right sons with y parent.getRight().getRight())
		   deletedNodeSuccessor.setLeftNode(rightNode.getLeft());// attaching z left sons with y parent.getRight().getLeft())
		   parent.setRightNode(deletedNodeSuccessor);//attaching z parent to y
		   //now continue to rebalance after deletion  
	   }	   
  	}
  while (!isBalanced(parent)) {
	   numOfRebalance+= rebalanceAfterDeletion(parent);
	   parent=(WAVLNode) TreePosition(this.getRoot(), parent.getKey());	   
  	} 
  return numOfRebalance;
  }
 
  /**private boolean isBalanced(WAVLNode parent)
   * return true iff the subtree of the input is balanced wavl tree**/
  private boolean isBalanced(WAVLNode parent) {
	   WAVLNode rightNode= (WAVLNode) parent.getRight();
	   WAVLNode leftNode= (WAVLNode) parent.getLeft();
	   if ( (parent.getRank()-leftNode.getRank()<1 || parent.getRank()-leftNode.getRank()>2 ||
			   parent.getRank()-rightNode.getRank()<1 || parent.getRank()-rightNode.getRank()>2)) {
		   return false;
	   }
	return true;
  }
  
  /**private int rebalanceAfterDeletion(WAVLNode parent)
   * rebalance after deletion steps for the current position - parent node**/
  private int rebalanceAfterDeletion(WAVLNode parent) {//rebalance after delete
	   WAVLNode rightNode= (WAVLNode) parent.getRight();
	   WAVLNode leftNode= (WAVLNode) parent.getLeft();
	   if ( (parent.getRank()-leftNode.getRank()==3 && parent.getRank()-rightNode.getRank()==2) ||
			   (parent.getRank()-leftNode.getRank()==2 && parent.getRank()-rightNode.getRank()==3)  ) { //case (3,2) or case (2,3)
		   parent.setRank(parent.getRank()-1);
		   return 1;
	   }
	   else {// case (3,1) or case (1,3)
		   WAVLNode rightOfRight= (WAVLNode) rightNode.getRight();
		   WAVLNode leftOfRight= (WAVLNode) rightNode.getLeft();
		   WAVLNode rightOfLeft= (WAVLNode) leftNode.getRight();
		   WAVLNode leftOfLeft= (WAVLNode) leftNode.getLeft();
		   if ( parent.getRank()-leftNode.getRank()==3 && parent.getRank()-rightNode.getRank()==1){ // case (3,1)
			   if (rightNode.getRank()-rightOfRight.getRank()==2 && rightNode.getRank()-leftOfRight.getRank()==2) {// case (2,2)
				   parent.setRank(parent.getRank()-1);//demote z
				   rightNode.setRank(rightNode.getRank()-1);//demote y
				   return 2;
			   }
			   else if(rightNode.getRank()-rightOfRight.getRank()==1) { // case (1 or 2, 1)
				   WAVLNode parentOfParent= (WAVLNode) TreePosition(this.getRoot(), parent.getKey());
				   rightNode.setRank(rightNode.getRank()+1);// promote y
				   parent.setRank(parent.getRank()-1);//demote z
				   WAVLNode zPointer= (WAVLNode) parent;
				   if(parent.getKey()==this.getRoot().getKey()) {//parent is the root
					   this.root= leftRotate(new WAVLTree_origlassman_itaylevi (parent));
					   parent=this.root;
				   }
				   else {
					   if (parent.getKey()<parentOfParent.getKey()) {//attach as left child of parentOfParent
						   parentOfParent.setLeftNode( leftRotate(new WAVLTree_origlassman_itaylevi (parent)) );
						   parent= (WAVLNode) parentOfParent.getLeft();
					   }
					   else {//attach as right child of parentOfParent
						   parentOfParent.setRightNode( leftRotate(new WAVLTree_origlassman_itaylevi (parent)) );
						   parent= (WAVLNode) parentOfParent.getRight();
					   }
				   }  
				   if (zPointer.getRank()==1 && !zPointer.getLeft().isRealNode() && !zPointer.getRight().isRealNode()) {//zPointer (z) is a (2,2) leaf 
					   zPointer.setRank(zPointer.getRank()-1);//demote z
					   return 4;
				   }
				   else {
					   return 3;
				   }
			   }
			   else {//  case (1, 2)
				  WAVLNode parentOfParent= (WAVLNode) TreePosition(this.getRoot(), parent.getKey());
				  parent.setRank(parent.getRank()-2);//double demote z
				  rightNode.setRank(rightNode.getRank()-1);// demote y
				  leftOfRight.setRank(leftOfRight.getRank()+2);//double promote a
				  parent.setRightNode(rightRotate(new WAVLTree_origlassman_itaylevi (rightNode)));				  
				  if(parent.getKey()==this.getRoot().getKey()) {//parent is the root
					  this.root= leftRotate(new WAVLTree_origlassman_itaylevi (parent));
					  parent=this.root;
				  }
				  else {
					  if (parent.getKey()<parentOfParent.getKey()) {//attach as left child of parentOfParent
						   parentOfParent.setLeftNode( leftRotate(new WAVLTree_origlassman_itaylevi (parent)) );
						   parent= (WAVLNode) parentOfParent.getLeft();
					   }
					  else {//attach as right child of parentOfParent
						   parentOfParent.setRightNode( leftRotate(new WAVLTree_origlassman_itaylevi (parent)) );
						   parent= (WAVLNode) parentOfParent.getRight();
					   }
				  }				  
				  return 7;
			   }
		   }
		   
		   else if  (parent.getRank()-leftNode.getRank()==1 && parent.getRank()-rightNode.getRank()==3){// case (1,3)
			   if (leftNode.getRank()-rightOfLeft.getRank()==2 && leftNode.getRank()-leftOfLeft.getRank()==2) {// case (2,2)
				   parent.setRank(parent.getRank()-1);
				   leftNode.setRank(leftNode.getRank()-1);
				   return 2;
			   }
			   else if(leftNode.getRank()-leftOfLeft.getRank()==1) { // case (1 , 1 or 2)
				   WAVLNode parentOfParent= (WAVLNode) TreePosition(this.getRoot(), parent.getKey());
				   leftNode.setRank(leftNode.getRank()+1);// promote y
				   parent.setRank(parent.getRank()-1);//demote z
				   WAVLNode zPointer= (WAVLNode) parent;
				   if(parent.getKey()==this.getRoot().getKey()) {//parent is the root
					   this.root= rightRotate(new WAVLTree_origlassman_itaylevi (parent));
					   parent= this.root;
				   }
				   else {
					   if (parent.getKey()<parentOfParent.getKey()) {//attach as left child of parentOfParent
						   parentOfParent.setLeftNode( rightRotate(new WAVLTree_origlassman_itaylevi (parent)) );
						   parent= (WAVLNode) parentOfParent.getLeft();
					   }
					   else {//attach as right child of parentOfParent
						   parentOfParent.setRightNode( rightRotate(new WAVLTree_origlassman_itaylevi (parent)) );
						   parent= (WAVLNode) parentOfParent.getRight();
					   }   
				   }				   
				   if (zPointer.getRank()==1 && !zPointer.getLeft().isRealNode() && !zPointer.getRight().isRealNode()) {//zPointer (z) is a (2,2) leaf 
					   zPointer.setRank(zPointer.getRank()-1);//demote z
					   return 4;
				   }
				   else {
					   return 3;
				   }  
			   }
			  else {//  case (2, 1)
				  WAVLNode parentOfParent= (WAVLNode) TreePosition(this.getRoot(), parent.getKey());
				  parent.setRank(parent.getRank()-2);//double demote z
				  leftNode.setRank(leftNode.getRank()-1);// demote y
				  rightOfLeft.setRank(rightOfLeft.getRank()+2);//double promote a
				  parent.setLeftNode(leftRotate(new WAVLTree_origlassman_itaylevi (parent))); 
				  if(parent.getKey()==this.getRoot().getKey()) {//parent is the root
					  this.root= rightRotate(new WAVLTree_origlassman_itaylevi (parent));
					  parent= this.root;
				  }
				  else {
					  if (parent.getKey()<parentOfParent.getKey()) {//attach as left child of parentOfParent
						   parentOfParent.setLeftNode( rightRotate(new WAVLTree_origlassman_itaylevi (parent)) );
						   parent= (WAVLNode) parentOfParent.getLeft();
					   }
					  else {//attach as right child of parentOfParent
						   parentOfParent.setRightNode( rightRotate(new WAVLTree_origlassman_itaylevi (parent)) );
						   parent= (WAVLNode) parentOfParent.getRight();
					   }
				  }				  
				  return 2;
			   }			   
		   }
	   }
		 return 0;
  }
  
/** public IWAVLNode successor(IWAVLNode node)
 * retrieves the successor node of the input node**/
public IWAVLNode successor(IWAVLNode node) {
	   WAVLNode x= (WAVLNode) node.getRight();
	   if (x.getRank()!=-1) {
		   return (findMin(x));
	   }
	   WAVLNode y= (WAVLNode) TreePosition(this.getRoot(), x.getKey());  
	   while(y.getRank()!=-1 && x.getKey()==y.getRight().getKey()) {
		   x=y;
			y=(WAVLNode) TreePosition(this.getRoot(), x.getKey());  
		}		  
		  return y;
	  }
  
	/**public IWAVLNode minNode(WAVLNode node)
	 * retrieves minNode in O(1)**/
  public IWAVLNode minNode(WAVLNode node) {
	  return this.minN;
  }
  
  /**private WAVLNode findMin(WAVLNode node)
   * returns the node with the minimal key in the subtree of this node
   * makes a full search trough the subtree of input node**/
  private WAVLNode findMin(WAVLNode node) {
	   WAVLNode tmp = node;
	   while(tmp.getLeft().isRealNode()) {
		   tmp=(WAVLNode) tmp.getLeft();
	   }
	   return tmp;
  }
  

  /**public IWAVLNode maxNode(WAVLNode node)
	 * retrieves maxNode in O(1)**/
  public IWAVLNode maxNode(WAVLNode node) {
	   return this.maxN;
  }
	
  /**private WAVLNode findMax(WAVLNode node)
   * returns the node with the max key in the subtree of this node
   * makes a full search trough the subtree of input node**/
  private WAVLNode findMax(WAVLNode node) {
	  	WAVLNode tmp = (WAVLNode) node;
	  	while(tmp.getRight().isRealNode()) {
		   tmp=(WAVLNode) tmp.getRight();
	  	}
	  	return tmp;
  	}
  
 
/**
   * public String min()
   *
   * Returns the info of the item with the smallest key in the tree,
   * or null if the tree is empty
   */
  public String min()
  {
	   return this.minN.getValue();
  }

  /**
   * public String max()
   *
   * Returns the info of the item with the largest key in the tree,
   * or null if the tree is empty
   */
  public String max()
  {
	   return this.maxN.getValue();
  }


 /**
  * public int[] keysToArray()
  *
  * Returns a sorted array which contains all keys in the tree,
  * or an empty array if the tree is empty.
  */
  public int[] keysToArray()
  {
       if (root==null) {// empty tree
       	return new int[] {};
       }
       List<Integer> keysList = new ArrayList<>();
       recKeysToArray(keysList);   
       return intListToArray(keysList);
 }

  /**public void recKeysToArray(List<Integer> lst) 
   * recursively makes 'in order' walk and updates a list with ordered nodes keys info (nodes ordered by key)  **/
  public void recKeysToArray(List<Integer> lst) {
	   if (this.getRoot().isRealNode()) {		   
		   WAVLNode subLeftRoot= (WAVLNode) this.getRoot().getLeft();
		   WAVLTree_origlassman_itaylevi subLeft = new WAVLTree_origlassman_itaylevi(subLeftRoot);
		   subLeft.recKeysToArray(lst);
		   lst.add(this.getRoot().getKey());
		   WAVLNode subRightRoot= (WAVLNode) this.getRoot().getRight();
		   WAVLTree_origlassman_itaylevi subRight = new WAVLTree_origlassman_itaylevi(subRightRoot);
		   subRight.recKeysToArray(lst);	   
	   }
  }
  
  /**private int[] intListToArray(List<Integer> keysList)
   * retrieves int[] array type with inputs list values **/
  private int[] intListToArray(List<Integer> keysList) {
		int[] keysArr= new int[keysList.size()];
		for (int i=0 ; i < keysArr.length; i++) {
			keysArr[i]= keysList.get(i);
		}
		return keysArr;
	  }

  /**private String[] strListToArray(List<String> valList)
   * retrieves String[] array type with inputs list values **/
  private String[] strListToArray(List<String> valList) {
		String[] valArr= new String[valList.size()];
		for (int i=0 ; i < valArr.length; i++) {
			valArr[i]= valList.get(i);
		}
		return valArr;
	  }   

  
  
 /**
  * public String[] infoToArray()
  *
  * Returns an array which contains all info in the tree,
  * sorted by their respective keys,
  * or an empty array if the tree is empty.
  */
  public String[] infoToArray()
  {
	   if (root==null) { // empty tree
      	return new String[] {};
      }
      List<String> valList = new ArrayList<>();
      recInfoToArray(valList);   
      return strListToArray(valList);
  }

  /**private void recInfoToArray(List<String> infoList)
   * recursively makes 'in order' walk and updates a list with ordered nodes info (nodes ordered by key)**/
  private void recInfoToArray(List<String> infoList) {
	   if (this.getRoot().isRealNode()) {
		   
		   WAVLNode subLeftRoot= (WAVLNode) this.getRoot().getLeft();
		   WAVLTree_origlassman_itaylevi subLeft = new WAVLTree_origlassman_itaylevi(subLeftRoot);
		   subLeft.recInfoToArray(infoList);
		   infoList.add(this.getRoot().getValue());
		   WAVLNode subRightRoot= (WAVLNode) this.getRoot().getRight();
		   WAVLTree_origlassman_itaylevi subRight = new WAVLTree_origlassman_itaylevi(subRightRoot);
		   subRight.recInfoToArray(infoList);
	   }
  }
  
  /**private void subTreeSizeUpdate(IWAVLNode treeRoot, WAVLNode node, int diff)
   * updates all subtreeSize in path from input node to the treeRoot with +diff **/
  private void subTreeSizeUpdate(IWAVLNode treeRoot, WAVLNode node, int diff) {
	  while(node.getKey()!= treeRoot.getKey()) {
		  node.setSubtreeSize((node.getSubtreeSize()+diff));
		  node= (WAVLNode) TreePosition((WAVLNode)treeRoot, node.getKey());
	  }
	  node.setSubtreeSize((node.getSubtreeSize()+diff));
  }


  /**
   * public int size()
   *
   * Returns the number of nodes in the tree.
   *
   * precondition: none
   * postcondition: none
   */
  public int size()
  {
	   return this.getRoot().getSubtreeSize();
  }
  
    /**
   * public int getRoot()
   *
   * Returns the root WAVL node, or null if the tree is empty
   *
   * precondition: none
   * postcondition: none
   */
  public IWAVLNode getRoot()
  {
	   return this.root;
  }
    /**
   * public int select(int i)
   *
   * Returns the value of the i'th smallest key (return -1 if tree is empty)
   * Example 1: select(1) returns the value of the node with minimal key 
	* Example 2: select(size()) returns the value of the node with maximal key 
	* Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor 	
   *
	* precondition: size() >= i > 0
   * postcondition: none
   */   
  public String select(int i)
  {
	  
	  int r= this.getRoot().getLeft().getSubtreeSize()+2; 
	   if (i==r) {
		   return this.getRoot().getValue();
	   }
	   else if(i<r) {
		   WAVLTree_origlassman_itaylevi tmpTree= new WAVLTree_origlassman_itaylevi((WAVLNode) this.getRoot().getLeft());
		   return  tmpTree.select(i);
	   }
	   else {
		   WAVLTree_origlassman_itaylevi tmpTree= new WAVLTree_origlassman_itaylevi((WAVLNode) this.getRoot().getRight());
		   return tmpTree.select(i-r);
	   }
	   
  }
	

	/**
	   * public interface IWAVLNode
	   * ! Do not delete or modify this - otherwise all tests will fail !
	   */
	public interface IWAVLNode{	
		public int getKey(); //returns node's key (for virtuval node return -1)
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public IWAVLNode getLeft(); //returns left child (if there is no left child return null)
		public IWAVLNode getRight(); //returns right child (if there is no right child return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		public int getSubtreeSize(); // Returns the number of real nodes in this node's subtree (Should be implemented in O(1))
	}

  /**
  * public class WAVLNode
  *
  * If you wish to implement classes other than WAVLTree
  * (for example WAVLNode), do it in this file, not in 
  * another file.
  * This class can and must be modified.
  * (It must implement IWAVLNode)
  */
 public class WAVLNode implements IWAVLNode{
	  
	    private int key;
		private String value;
		private int rank=-1;
		private IWAVLNode leftNode = null;
		private IWAVLNode rightNode= null;
		private int subTreeSize;
		
		public WAVLNode(int key,String value) {
			this.key=key;
			this.value= value;
		}
		public int getRank() {
			return this.rank;
		}
		public void setRank(int rank) {
			this.rank=rank;
		}
		public int getKey()
		{
			return this.key;
		}
		public String getValue()
		{
			return this.value;
		}
		public void setValue(String value) {
			this.value=value;
		}
		public void setLeftNode(IWAVLNode node) {
			this.leftNode = node;
		}
		public void setRightNode(IWAVLNode node) {
			this.rightNode = node;
		}
		public IWAVLNode getLeft()
		{
			if (this.leftNode==null) {
				return (new WAVLNode(-1,null));
			}
			return this.leftNode;
		}
		public IWAVLNode getRight()
		{
			if (this.rightNode==null) {
				return (new WAVLNode(-1,null));
			}
			return this.rightNode;
		}
		// Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)
		public boolean isRealNode()
		{
			return (this.rank>=0);
			//return ( (this.rightNode!=null && this.rightNode!=null) );
		}

		public int getSubtreeSize()
		{
			if(!this.isRealNode()) {
				return -1;
			}
			return this.subTreeSize;
		}
		
		public void setSubtreeSize(int size)
		{
			this.subTreeSize= size;
		}
 }
}







