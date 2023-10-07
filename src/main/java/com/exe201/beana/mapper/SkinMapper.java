package com.exe201.beana.mapper;

import com.exe201.beana.dto.SkinDto;
import com.exe201.beana.entity.Skin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SkinMapper {
    SkinMapper INSTANCE = Mappers.getMapper(SkinMapper.class);

    SkinDto toSkinDto(Skin skin);

    Skin toSkin(SkinDto skinDto);
}
