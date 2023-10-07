package com.exe201.beana.service;

import com.exe201.beana.dto.SkinDto;
import com.exe201.beana.dto.SkinRequestDto;

import java.util.List;

public interface SkinService {
    SkinDto addSkin(SkinRequestDto skinRequest);

    List<SkinDto> getAllSkins();
}
