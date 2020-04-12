/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author gem
 */
public class QueueNotFoundException extends Exception {

    public QueueNotFoundException() {
    }

    public QueueNotFoundException(String string) {
        super(string);
    }
    
}
