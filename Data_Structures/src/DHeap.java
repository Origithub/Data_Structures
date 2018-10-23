/**Name: Ori Glassman, ID: 311453427, User Name: origlassman. Name: Itay Levi, ID: 312485386, User Name: itaylevi */



/**
 * D-Heap
 */

public class DHeap
{
	
    private int size, max_size, d;
    private DHeap_Item[] array;

	// Constructor
	// m_d >= 2, m_size > 0
    DHeap(int m_d, int m_size) {
               max_size = m_size;
			   d = m_d;
               array = new DHeap_Item[max_size];
               size = 0;
    }
	
	/**
	 * public int getSize()
	 * Returns the number of elements in the heap.
	 */
	public int getSize() {
		return size;
	}
	
  /**
     * public int arrayToHeap()
     *
     * The function builds a new heap from the given array.
     * Previous data of the heap should be erased.
     * preconidtion: array1.length() <= max_size
     * postcondition: isHeap()
     * 				  size = array.length()
     * Returns number of comparisons along the function run. 
	 */
    public int arrayToHeap(DHeap_Item[] array1) 
    {
    	this.size= array1.length;
    	this.array=array1.clone();
    	int compCnt=0;
    	for (int i=parent(this.getSize()-1, this.d); i>=0; i--) {
    		compCnt+=heapifyDown(this ,i);
    	}
        return compCnt;
    }

    
    
    private static int heapifyUp(DHeap h, int i) {
    	int parentIndex;int compCnt=0;
    	while (i>0 && h.array[i].getKey()<h.array[(parentIndex=parent(i,h.d))].getKey()) {
    		DHeap_Item tmp= h.array[i];
    		h.array[i]= h.array[parentIndex];
    		h.array[i].setPos(i);
    		h.array[parentIndex]= tmp;
    		h.array[parentIndex].setPos(parentIndex);
    		i=parentIndex;
    		compCnt++;
    	}
    	return compCnt;
    }
    private static int heapifyDown(DHeap h, int i) {
    	int compCnt=0;int smallest=i;int smallestKey= h.array[i].getKey();
    	int tmpKey; int childIndex;
    	for (int j=1; j<=h.d; j++) {
    		if ( (childIndex=child(i, j, h.d) )>=h.getSize()) {
    			break;
    		}
    		compCnt++;
    		if((tmpKey=h.array[childIndex].getKey())<smallestKey) {
    			smallestKey= tmpKey;
    			smallest= childIndex;
    		}
    	}
    	if (smallest>i) {
    		DHeap_Item tmp= h.array[i];
    		h.array[i]= h.array[smallest];
    		h.array[i].setPos(i);
    		h.array[smallest]= tmp;
    		h.array[smallest].setPos(smallest);
    		compCnt+= heapifyDown(h, smallest);
    	}
    return compCnt;
	}
    /**
     * public boolean isHeap()
     *
     * The function returns true if and only if the D-ary tree rooted at array[0]
     * satisfies the heap property or has size == 0.
     *   
     */
    public boolean isHeap() 
    {
    	int childIndex;
    	for (int i=0 ; i < this.getSize() ; i++) {
    		for (int j=1 ; j <= this.d ; j++) {
    			if( (childIndex=child(i, j, this.d))>=this.getSize() ) {
    				return true;
    			}
    			if ( array[i].getKey()>array[childIndex].getKey() ) {
    				return false;
    			}
    		}
    	}
        return true; 
    }


 /**
     * public static int parent(i,d), child(i,k,d)
     * (2 methods)
     *
     * precondition: i >= 0, d >= 2, 1 <= k <= d
     *
     * The methods compute the index of the parent and the k-th child of 
     * vertex i in a complete D-ary tree stored in an array. 
     * Note that indices of arrays in Java start from 0.
     */
    public static int parent(int i, int d) {
    	return Math.floorDiv(i-1, d);
    } 
    public static int child (int i, int k, int d) {
    	return i*d+k;
    }

    /**
    * public int Insert(DHeap_Item item)
    *
	* Inserts the given item to the heap.
	* Returns number of comparisons during the insertion.
	*
    * precondition: item != null
    *               isHeap()
    *               size < max_size
    * 
    * postcondition: isHeap()
    */
    public int Insert(DHeap_Item item) 
    {        
    	this.size++;
    	this.array[this.size-1]=item;
    	item.setPos(this.size-1);
    	int compCnt=heapifyUp(this,item.getPos());
    	return compCnt;
    }

 /**
    * public int Delete_Min()
    *
	* Deletes the minimum item in the heap.
	* Returns the number of comparisons made during the deletion.
    * 
	* precondition: size > 0
    *               isHeap()
    * 
    * postcondition: isHeap()
    */
    public int Delete_Min()
    {
     	this.array[0]=this.array[this.getSize()-1];
     	this.size--; 
     	this.array[0].setPos(0);
     	int numOfCompares = heapifyDown(this, 0);
     	return numOfCompares;
    }


    /**
     * public DHeap_Item Get_Min()
     *
	 * Returns the minimum item in the heap.
	 *
     * precondition: heapsize > 0
     *               isHeap()
     *		size > 0
     * 
     * postcondition: isHeap()
     */
    public DHeap_Item Get_Min()
    {
	return array[0];
    }
	
  /**
     * public int Decrease_Key(DHeap_Item item, int delta)
     *
	 * Decerases the key of the given item by delta.
	 * Returns number of comparisons made as a result of the decrease.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Decrease_Key(DHeap_Item item, int delta)
    {
    	item.setKey(item.getKey()-delta);
    	return heapifyUp(this, item.getPos());    
    }
	
	  /**
     * public int Delete(DHeap_Item item)
     *
	 * Deletes the given item from the heap.
	 * Returns number of comparisons during the deletion.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Delete(DHeap_Item item)
    {
    	this.Decrease_Key(item, Math.abs(Integer.MIN_VALUE+item.getKey()));
    	return this.Delete_Min();
    }
	
	/**
	* Sort the input array using heap-sort (build a heap, and 
	* perform n times: get-min, del-min).
	* Sorting should be done using the DHeap, name of the items is irrelevant.
	* 
	* Returns the number of comparisons performed.
	* 
	* postcondition: array1 is sorted 
	*/
	public static int DHeapSort(int[] array1, int d) {
		DHeap_Item[] heapArray = new DHeap_Item[array1.length];
		for (int i=0 ; i < array1.length ; i++) {
			heapArray[i] = new DHeap_Item("", array1[i]);
		}
		DHeap h = new DHeap(d,array1.length);
		int compCnt=0;
		compCnt+=h.arrayToHeap(heapArray);
		for (int i=0; i < array1.length ; i++) {
			DHeap_Item tempMin = h.Get_Min();
			compCnt+=h.Delete_Min();
			array1[i]= tempMin.getKey();
		}
		return compCnt;
	}
}
