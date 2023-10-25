package nextech.shoploc.utils;

import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;

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
                .toList();

    }


}
