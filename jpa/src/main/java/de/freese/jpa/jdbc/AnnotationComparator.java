/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose Tools | Templates and open the template in
 * the editor.
 */
package de.freese.jpa.jdbc;

import java.lang.annotation.Annotation;
import java.util.Comparator;

/**
 * @author Thomas Freese
 */
public class AnnotationComparator implements Comparator<Annotation>
{
    /**
     *
     */
    public AnnotationComparator()
    {
        super();
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final Annotation o1, final Annotation o2)
    {
        return o1.toString().compareTo(o2.toString());
    }
}
