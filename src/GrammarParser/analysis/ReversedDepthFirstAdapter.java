/* This file was generated by SableCC (http://www.sablecc.org/). */

package GrammarParser.analysis;

import java.util.*;
import GrammarParser.node.*;


public class ReversedDepthFirstAdapter extends AnalysisAdapter
{
    final List<Void> dummy = new ArrayList<Void>();

    public void inStart(Start node)
    {
        defaultIn(node);
    }

    public void outStart(Start node)
    {
        defaultOut(node);
    }

    public void defaultIn(  Node node)
    {
        // Do nothing
    }

    public void defaultOut(  Node node)
    {
        // Do nothing
    }

    @Override
    public void caseStart(Start node)
    {
        inStart(node);
        node.getEOF().apply(this);
        node.getPRoot().apply(this);
        outStart(node);
    }

    public void inARoot(ARoot node)
    {
        defaultIn(node);
    }

    public void outARoot(ARoot node)
    {
        defaultOut(node);
    }

    @Override
    public void caseARoot(ARoot node)
    {
        inARoot(node);
        {
            List<PRule> copy = new ArrayList<PRule>(node.getRule());
            Collections.reverse(copy);
            for(PRule e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getSymbols() != null)
        {
            node.getSymbols().apply(this);
        }
        outARoot(node);
    }

    public void inASymbols(ASymbols node)
    {
        defaultIn(node);
    }

    public void outASymbols(ASymbols node)
    {
        defaultOut(node);
    }

    @Override
    public void caseASymbols(ASymbols node)
    {
        inASymbols(node);
        if(node.getStartSymbol() != null)
        {
            node.getStartSymbol().apply(this);
        }
        {
            List<TIdentifier> copy = new ArrayList<TIdentifier>(node.getNonterminals());
            Collections.reverse(copy);
            for(TIdentifier e : copy)
            {
                e.apply(this);
            }
        }
        {
            List<TSymbol> copy = new ArrayList<TSymbol>(node.getTerminals());
            Collections.reverse(copy);
            for(TSymbol e : copy)
            {
                e.apply(this);
            }
        }
        outASymbols(node);
    }

    public void inARule(ARule node)
    {
        defaultIn(node);
    }

    public void outARule(ARule node)
    {
        defaultOut(node);
    }

    @Override
    public void caseARule(ARule node)
    {
        inARule(node);
        {
            List<TSymbol> copy = new ArrayList<TSymbol>(node.getGoingTo());
            Collections.reverse(copy);
            for(TSymbol e : copy)
            {
                e.apply(this);
            }
        }
        if(node.getComingFrom() != null)
        {
            node.getComingFrom().apply(this);
        }
        outARule(node);
    }
}
