public class Jumble extends Seq{
    protected int num;
    protected int [] values;

    public Jumble(int [] values){
        num = values.length;
        this.values = new int [values.length];
        if(values != null && values.length > 0)
            System.arraycopy(values, 0, this.values, 0, values.length);
    }

    public String toString(){
        String output = "{ " + num + " : ";
        
        for(int ii = 0; ii < num; ++ii){
            output = output + values[ii] + " ";
        }

        output = output + "}";
        return output;
    }
}