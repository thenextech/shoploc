package nextech.shoploc.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
public class ModelMapperUtils {
    @Getter
    private final ModelMapper modelMapper = new ModelMapper();

    public ModelMapperUtils() {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {

        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());

    }


}
