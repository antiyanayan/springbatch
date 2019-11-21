package com.springtuts.springbatchdemo.jobdemo;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;

public class OrderSkipPolicy implements SkipPolicy {

	private static final Logger logger = LoggerFactory.getLogger(OrderSkipPolicy.class);
	
	@Override
	public boolean shouldSkip(Throwable exception, int skipCount) throws SkipLimitExceededException {
		if (exception instanceof FileNotFoundException) {
			return false;
		}

		else if (exception instanceof FlatFileParseException && skipCount < 1) {

			FlatFileParseException ffpe = (FlatFileParseException) exception;
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append(">>> An error occured at line " + ffpe.getLineNumber() + " while reading the input : \n"
					+ ffpe.getInput());
			logger.error("{}", errorMessage.toString());
			return true;
		} else {
			return false;
		}
	}

}
