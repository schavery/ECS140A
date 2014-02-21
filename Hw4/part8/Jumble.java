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

    public int min() {
        if(num == 0) {
            return 0;
        } else {
            int min = Integer.MAX_VALUE;
            for(int ii = 0; ii < values.length; ++ii) {
                min = Math.min(min, values[ii]);
            }
            return min;
        }
    }

    public SeqIt createSeqIt() {
        return new JumbleIt(this);
    }
}