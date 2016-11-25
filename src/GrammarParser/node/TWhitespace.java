/* This file was generated by SableCC (http://www.sablecc.org/). */

package GrammarParser.node;

import GrammarParser.analysis.*;

 
public final class TWhitespace extends Token
{
    public TWhitespace(String text)
    {
        setText(text);
    }

    public TWhitespace(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TWhitespace(getText(), getLine(), getPos());
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTWhitespace(this);
    }
}
