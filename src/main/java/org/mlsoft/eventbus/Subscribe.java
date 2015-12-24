package org.mlsoft.eventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)

/**
 * Subscribes a method to get events delivered.
 * Events are posted to the registered event bus get delivered to the subscribed methods.
 * The events are dispatched by a executor service which uses multiple threads. Therefore normally there is
 * no guarantee in which order the events get delivered.
 * If a client wants sequential order (order in which the events got posted) the flag sequential should be set.
 * In that case the subscriber is also guaranteed to not get two different events on the same time.
 */
public @interface Subscribe {
	/** should the event dispatching be on the awt event thread? This is useful if the
	 * event handling method works with awt or swing models. */
	boolean onETD() default false;
	/** should the events get guaranteed delivered in sequential order of their posting to the event handler? */
	boolean sequential() default false;
}
