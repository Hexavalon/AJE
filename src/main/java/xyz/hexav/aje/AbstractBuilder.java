package xyz.hexav.aje;

import xyz.hexav.aje.operators.Operator;
import xyz.hexav.aje.operators.Precedence;
import xyz.hexav.aje.pool.Pool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public abstract class AbstractBuilder<T extends AbstractBuilder<T>>
{
    private final List<String> lines = new ArrayList<>();
    
    private Pool pool;
    
    public T addLine(String script)
    {
        lines.add(script);
        return (T) this;
    }
    
    public T addLine(int i, String script)
    {
        lines.add(i, script);
        return (T) this;
    }
    
    public T removeLine(int i)
    {
        lines.remove(i);
        return (T) this;
    }
    
    public T setPool(Pool pool)
    {
        this.pool = pool;
        return (T) this;
    }
    
    public T addVariable(String... variables)
    {
        return addVariable(Arrays.asList(variables));
    }
    
    public T addVariable(List<String> variables)
    {
        for (String var : variables)
        {
            getPool().allocVar(var);
        }
        return (T) this;
    }
    
    public T addFunction(Function function)
    {
        getPool().allocFunc(function);
        return (T) this;
    }
    
    public T addOperator(Operator operator)
    {
        return addOperator(Precedence.INFIX, operator);
    }
    
    public T addOperator(int precedence, Operator operator)
    {
        getPool().getOperators().register(precedence, operator);
        return (T) this;
    }
    
    public Pool getPool()
    {
        return pool != null ? pool : (pool = Pool.getDefaultPool());
    }
    
    public List<String> getLines()
    {
        return lines;
    }
}
