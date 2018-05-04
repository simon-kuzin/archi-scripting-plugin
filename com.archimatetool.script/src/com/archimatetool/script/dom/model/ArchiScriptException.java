/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.script.dom.model;

/**
 * Archi Script Exception
 * 
 * @author Phillip Beauvoir
 */
public class ArchiScriptException extends RuntimeException {

    public ArchiScriptException(String message) {
        super(message);
        System.err.println(this);
    }

    public ArchiScriptException() {
        super();
    }

    public ArchiScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArchiScriptException(Throwable cause) {
        super(cause);
    }
    
}