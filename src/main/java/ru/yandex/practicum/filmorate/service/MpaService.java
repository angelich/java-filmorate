package ru.yandex.practicum.filmorate.service;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@Service
public class MpaService {
    MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public List<MPA> getAllMpa(){
        return mpaStorage.getAllMpa();
    }

    public MPA getMpaOrThrow(Long mpaId){
        return mpaStorage.getMpaOrThrow(mpaId);
    }
}
