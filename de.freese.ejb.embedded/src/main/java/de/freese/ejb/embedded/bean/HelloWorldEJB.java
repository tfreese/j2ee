/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.freese.ejb.embedded.bean;

import javax.ejb.Stateless;

/**
 *
 * @author Thomas Freese
 */
@Stateless
public class HelloWorldEJB implements IHelloWorldLocal
{
    /**
     *
     */
    public HelloWorldEJB()
    {
        super();
    }

    /**
     *
     * @return
     */
    @Override
    public String getHelloWorld()
    {
        return "Hello World";
    }
}
