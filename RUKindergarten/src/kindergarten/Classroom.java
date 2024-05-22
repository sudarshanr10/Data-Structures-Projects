package kindergarten;
/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;         // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;          // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingLocation;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;  // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine  = l;
        musicalChairs   = m;
		seatingLocation = a;
        studentsSitting = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students standing in line and coming into the classroom (not leaving the line).
     * 
     * It does this by reading students from input file and inserting these students studentsInLine singly linked list.
     * 
     * 1. Open the file using StdIn.setFile(filename);
     * 
     * 2. For each line of the input file:
     *     1. read a student from the file
     *     2. create an object of type Student with the student information
     *     3. insert the Student object at the FRONT of the linked list
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * To read a string using StdIn use StdIn.readString()
     * 
     * The input file has Students in REVERSE alphabetical order. So, at the end of this
     * method all students are in line in alphabetical order.
     * 
     * DO NOT implement a sorting method, PRACTICE add to front.
     * 
     * @param filename the student information input file
     */
    public void enterClassroom ( String filename ) {

        StdIn.setFile(filename);
        int length = StdIn.readInt();
        for(int i=0; i<length; i++)
        {
            String firstName = StdIn.readString();
            String lastName = StdIn.readString();
            int height = StdIn.readInt();
            Student student = new Student(firstName, lastName, height);
            studentsInLine = new SNode(student, studentsInLine);
        }
    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     *      
     * 1. Open the file using StdIn.setFile(seatingChart);
     * 
     * 2. You will read the seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true represents that a 
     * seat is present in that column)
     * 
     * 3. Initialize seatingLocation and studentsSitting arrays with r rows and c columns
     * 
     * 4. Update seatingLocation with the boolean values read from the input file
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {
        
        StdIn.setFile(seatingChart);
        int rows = StdIn.readInt();
        int cols = StdIn.readInt();
        seatingLocation = new boolean[rows][cols];
        studentsSitting = new Student[rows][cols];
        for(int i=0; i<seatingLocation.length; i++)
        {
            for(int j=0; j<seatingLocation[0].length; j++)
            {
                seatingLocation[i][j] = StdIn.readBoolean();
            }
        }
    }

    /**
     * 
     * This method simulates students standing inline and then taking their seats in the classroom.
     * 
     * 1. Starting from the front of the studentsInLine singly linked list
     * 2. Remove one student at a time from the list and insert the student into studentsSitting according to
     *    seatingLocations
     * 
     * studentsInLine will then be empty
     * 
     * If the students just played musical chairs, the winner of musical chairs is seated separately 
     * by seatMusicalChairsWinner().
     */
    public void seatStudents () {
        
        for(int i=0; i<studentsSitting.length; i++)
        {
            for(int j=0; j<studentsSitting[0].length; j++)
            {
                if(seatingLocation[i][j] == true)
                {
                    if(studentsInLine != null)
                    {
                        studentsSitting[i][j] = studentsInLine.getStudent();
                        studentsInLine = studentsInLine.getNext();
                    }
                }
            }
        }
    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {
        SNode ptr = new SNode();
        musicalChairs = new SNode();
        for(int i=0; i<studentsSitting.length; i++)
        {
            for(int j=0; j<studentsSitting[0].length; j++)
            {
                if(studentsSitting[i][j] != null)
                {
                    if(musicalChairs.getStudent() == null)
                    {
                        musicalChairs.setStudent(studentsSitting[i][j]);
                    }
                    else if(musicalChairs.getNext() == null)
                    {
                        ptr.setStudent(studentsSitting[i][j]);
                        musicalChairs.setNext(ptr);
                    }
                    else{
                        ptr.setNext(new SNode(studentsSitting[i][j], null));
                        ptr = ptr.getNext();
                    }
                }
                studentsSitting[i][j] = null;
            }
        }
        ptr.setNext(musicalChairs);
        musicalChairs = ptr;
    }

    /**
     * 
     * Removes a random student from the musicalChairs.
     * 
     * @param size represents the number of students currently sitting in musicalChairs
     * 
     * 1. Select a random student to be removed from the musicalChairs CLL.
     * 2. Searches for the selected student, deletes that student from musicalChairs
     * 3. Calls insertByHeight to insert the deleted student into studentsInLine list.
     * 
     * Requires the use of StdRandom.uniform(int b) to select a random student to remove,
     * where b is the number of students currently sitting in the musicalChairs.
     * 
     * The random value denotes the refers to the position of the student to be removed
     * in the musicalChairs. 0 is the first student
     */
    public void moveStudentFromChairsToLine(int size) {
        int n = StdRandom.uniform(size);
        SNode ptr = musicalChairs.getNext();
        SNode prev = musicalChairs;
        Student removed = null;
        int count = 0;
        while(count<=n && musicalChairs != null){
            if(count == n)
            {
                if(n == 0)
                {
                    removed = ptr.getStudent();
                    prev.setNext(ptr.getNext());
                    musicalChairs.setNext(prev.getNext());
                }
                if(n == size-1)
                {
                    removed = ptr.getStudent();
                    prev.setNext(ptr.getNext()); 
                    musicalChairs = prev;
                    musicalChairs.setNext(prev.getNext());
                }
                else{
                    removed = ptr.getStudent();
                    prev.setNext(ptr.getNext());
                }
            }
            prev = ptr;
            ptr = ptr.getNext();
            count++;
        }
        insertByHeight(removed);
    }

    /**
     * Inserts a single student, eliminated from musical chairs, to the studentsInLine list.
     * The student is inserted in ascending order by height (shortest to tallest).
     * 
     * @param studentToInsert the student eliminated from chairs to be inserted into studentsInLine
     */
    public void insertByHeight(Student studentToInsert) {
        SNode node = new SNode(studentToInsert, null);
        if(studentsInLine == null)
        {
            studentsInLine = node;
            return;
        }
        else if(studentToInsert.getHeight() <studentsInLine.getStudent().getHeight())
        {
            studentsInLine = new SNode(studentToInsert, studentsInLine);
            return;
        }
        else{
            SNode ptr = studentsInLine;
            while(ptr.getNext()!=null)
            {
                if(studentToInsert.getHeight() > ptr.getStudent().getHeight() && studentToInsert.getHeight() < ptr.getNext().getStudent().getHeight())
                {
                    ptr.setNext(new SNode(studentToInsert, ptr.getNext()));
                    return;
                }
                else if(studentToInsert.getHeight() == ptr.getStudent().getHeight() && studentToInsert.getHeight() != ptr.getNext().getStudent().getHeight())
                {
                    ptr.setNext(new SNode(studentToInsert, ptr.getNext()));
                    return;
                }
                ptr = ptr.getNext();
            }
            ptr.setNext(new SNode(studentToInsert, null));
        }
    }    

    /**
     * 
     * Removes eliminated students from the musicalChairs and inserts those students 
     * back in studentsInLine in ascending height order (shortest to tallest).
     * 
     * At the end of this method, all students will be in studentsInLine besides 
     * the winner, who will be in musicalChairs alone.
     * 
     * 1. Find the number of students currently on musicalChairs
     * 2. While there are more than 1 student in musicalChairs, call moveRandomStudentFromChairsToLine()
     */
    public void eliminateLosingStudents() {
        int size = 1;
        SNode ptr = musicalChairs;
        while(ptr.getNext()!= musicalChairs)
        {
            ptr = ptr.getNext();
            size++;
        }
        while(size>1)
        {
            moveStudentFromChairsToLine(size);
            size--;
        }
    }

    /* 
     * If musicalChairs (circular linked list) contains ONLY ONE student (the winner), 
     * this method removes the winner from musicalChairs and inserts that student 
     * into the first available seat in studentsSitting. musicChairs will then be empty.
     * 
     * This only happens when the musical chairs was just played.
     * 
     * This methods does nothing if there is more than one student in musicalChairs 
     * or if musicalChairs is empty.
     */
    public void seatMusicalChairsWinner () {
        boolean winner = false;
        Student student = null;
        if(musicalChairs != null && musicalChairs == musicalChairs.getNext())
        {
            for(int i = 0; i<seatingLocation.length; i++)
            {
                for(int j = 0; j<seatingLocation[0].length; j++)
                {
                    if(seatingLocation[i][j] == true && !winner)
                    {
                        student = musicalChairs.getStudent();
                        studentsSitting[i][j] = student;
                        musicalChairs = null;
                        seatingLocation[i][j] = false;
                        winner = true;
                    }
                }
            }
        }
    }

    /**
     * 
     * This method represents the game of musical chairs!
     * 
     * This method calls previously written methods to repeatedly remove students from 
     * the musicalChairs until there is only one student (the winner), seats the winner
     * and seat the students from the studentsInline.
     * 
     * *****DO NOT****** UPDATE THIS METHOD
     */
    public void playMusicalChairs() {

	/* DO NOT UPDATE THIS METHOD */
        eliminateLosingStudents();
        seatMusicalChairsWinner();
        seatStudents();
    } 

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingLocation[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingLocation() { return seatingLocation; }
    public void setSeatingLocation(boolean[][] a) { seatingLocation = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
