package sample;

public class CircularList {
    //Declaring head and tail pointer as null.
    public Node head = null;
    public Node tail = null;
    private int size = 0;
    //This function will add the new node at the end of the list.
    public void add(PositionId data){
        //Create new node
        Node newNode = new Node(data);
        //Checks if the list is empty.
        if(head == null) {
            //If list is empty, both head and tail would point to new node.
            head = newNode;
            tail = newNode;
            newNode.next = head;
            newNode.index = 0;
            size++;
        }
        else {
            //tail will point to new node.
            tail.next = newNode;
            newNode.index = size;
            //New node will become new tail.
            tail = newNode;
            //Since, it is circular linked list tail will point to head.
            tail.next = head;
            size++;
        }
    }

    public int size(){
        return size;
    }

    public PositionId get(int index){
        PositionId item = new PositionId(0,0);
        Node itr = head;
        int pointer = 0;
        while(itr != tail){
            if(itr.index ==  index){
                item = itr.data;
                break;
            }
            itr = itr.next;
        }
        return item;
    }
}
