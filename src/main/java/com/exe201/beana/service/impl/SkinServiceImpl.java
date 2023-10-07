package com.exe201.beana.service.impl;

import com.exe201.beana.dto.SkinDto;
import com.exe201.beana.dto.SkinRequestDto;
import com.exe201.beana.entity.Skin;
import com.exe201.beana.exception.ResourceNameAlreadyExistsException;
import com.exe201.beana.mapper.SkinMapper;
import com.exe201.beana.repository.SkinRepository;
import com.exe201.beana.service.SkinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkinServiceImpl implements SkinService {

    private final SkinRepository skinRepository;

    @Override
    public SkinDto addSkin(SkinRequestDto skinRequest) {
        Optional<Skin> foundSkin = skinRepository.findSkinByStatusAndName((byte) 1, skinRequest.getName());
        if (foundSkin.isPresent()) {
            throw new ResourceNameAlreadyExistsException("The skin has already existed with id: " + foundSkin.get().getId());
        }
        Skin newSkin = new Skin();
        newSkin.setName(skinRequest.getName());
        newSkin.setStatus((byte) 1);
        return SkinMapper.INSTANCE.toSkinDto(skinRepository.save(newSkin));
    }

    @Override
    public List<SkinDto> getAllSkins() {
        return skinRepository.findAllByStatus((byte) 1).stream().map(SkinMapper.INSTANCE::toSkinDto).collect(Collectors.toList());
    }
}
