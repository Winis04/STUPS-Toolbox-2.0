/* This file was generated by SableCC (http://www.sablecc.org/). */

package AutomatonParser.node;

import AutomatonParser.analysis.*;

 
public final class TLBracket2 extends Token
{
    public TLBracket2()
    {
        super.setText("{");
    }

    public TLBracket2(int line, int pos)
    {
        super.setText("{");
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TLBracket2(getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTLBracket2(this);
    }

    @Override
    public void setText(  String text)
    {
        throw new RuntimeException("Cannot change TLBracket2 text.");
    }
}
