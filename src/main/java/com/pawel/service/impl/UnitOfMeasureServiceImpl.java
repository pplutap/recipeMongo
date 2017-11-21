package com.pawel.service.impl;

import com.pawel.commands.UnitOfMeasureCommand;
import com.pawel.converters.UnitOfMeasureToUnitOfMeasureCommand;
import com.pawel.repositories.UnitOfMeasureRepository;
import com.pawel.service.UnitOfMeasureService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UnitOfMeasureServiceImpl implements UnitOfMeasureService {

	private final UnitOfMeasureRepository unitOfMeasureRepository;
	private final UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand;

	public UnitOfMeasureServiceImpl(UnitOfMeasureRepository unitOfMeasureRepository,
			UnitOfMeasureToUnitOfMeasureCommand unitOfMeasureToUnitOfMeasureCommand) {
		this.unitOfMeasureRepository = unitOfMeasureRepository;
		this.unitOfMeasureToUnitOfMeasureCommand = unitOfMeasureToUnitOfMeasureCommand;
	}

	@Override
	public Set<UnitOfMeasureCommand> listAllUoms() {
		return StreamSupport.stream(unitOfMeasureRepository.findAll()
				.spliterator(), false)
				.map(unitOfMeasureToUnitOfMeasureCommand::convert)
				.collect(Collectors.toSet());
	}
}
