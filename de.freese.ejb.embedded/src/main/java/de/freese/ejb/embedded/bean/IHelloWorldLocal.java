/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.freese.ejb.embedded.bean;

import javax.ejb.Local;

/**
 *
 * @author Thomas Freese
 */
@Local
public interface IHelloWorldLocal
{
    /**
     * @return String
     */
    public String getHelloWorld();
}
