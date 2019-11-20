package com.springtuts.batchsession.demojob;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;

public class OrdersFilterSkipPolicy implements SkipPolicy {

	private static final Logger logger = LoggerFactory.getLogger(OrdersFilterSkipPolicy.class);
	
	@Override
	public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {

		if (exception instanceof FileNotFoundException)
			return false;

		else if (exception instanceof FlatFileParseException && skipCount <= 2) {
			FlatFileParseException ffpe = (FlatFileParseException) exception;
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append(">>>>>>>>>>>>>>>>>>>> An error occured while processing the " + ffpe.getLineNumber()
					+ "line of the file. Below was the faulty input : \n");
			errorMessage.append(ffpe.getInput());
			logger.error("{}", errorMessage.toString());
			return true;
		} else if(exception instanceof NumberFormatException && skipCount <= 2) {
			FlatFileParseException ffpe = (FlatFileParseException) exception;
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append(">>>>>>>>>>>>>>>>>>>> An error occured while processing the " + ffpe.getLineNumber()
					+ "line of the file. Order Id was invalid for the input : \n");
			errorMessage.append(ffpe.getInput());
			logger.error("{}", errorMessage.toString());
			return true;
		}
		else
			return false;
	}

}
