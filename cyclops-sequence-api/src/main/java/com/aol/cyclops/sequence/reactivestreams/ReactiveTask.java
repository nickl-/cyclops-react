package com.aol.cyclops.sequence.reactivestreams;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import lombok.Value;
import lombok.experimental.Wither;

import org.jooq.lambda.tuple.Tuple3;
import org.reactivestreams.Subscription;

/**
 * Class that represents an active reactive-streams task
 * 
 * @author johnmcclean
 *
 */
@Value
public class ReactiveTask implements Subscription{
	@Wither
	Executor exec;
	@Wither
	Tuple3<CompletableFuture<Subscription>,CompletableFuture<?>,CompletableFuture<Boolean>> subscriptionAndTask;

	/* (non-Javadoc)
	 * @see org.reactivestreams.Subscription#cancel()
	 */
	public void cancel(){
		this.subscriptionAndTask.v1.join().cancel();
	}
	/* (non-Javadoc)
	 * @see org.reactivestreams.Subscription#request(long)
	 */
	public void request(long n){
		this.subscriptionAndTask.v1.join().request(n);
	}
	/**
	 * @return true if current task is complete
	 */
	public boolean isCurrentTaskComplete(){
		return subscriptionAndTask.v2.isDone();
	}
	/**
	 * @return true if the entire Stream has been processed
	 */
	public boolean isStreamComplete(){
		return subscriptionAndTask.v3.isDone();
	}
	/**
	 * Asyncrhonously request more elements from the Stream
	 * 
	 * @param n Number of elements to request
	 * @return New ReactiveTask that references the execution of the new async task
	 */
	public ReactiveTask requestAsync(long n){
		return this.withSubscriptionAndTask(subscriptionAndTask
							.map2(c->CompletableFuture.runAsync(()->this.subscriptionAndTask.v1.join().request(n),exec)));
	}
	
	/**
	 * Request all elements to be processed from the processing Stream
	 */
	public void requestAll(){
		request(Long.MAX_VALUE);
	}
	/**
	 * Request all elements to be processed asynchronously in the processing stream
	 * 
	 * @return New ReactiveTask that references the execution of the new async task
	 */
	public ReactiveTask requestAllAsync(){
		return requestAsync(Long.MAX_VALUE);
	}
	
	/**
	 * Block until the currently active reactive task completes
	 */
	public void block(){
		subscriptionAndTask.v2.join();
	}
}
