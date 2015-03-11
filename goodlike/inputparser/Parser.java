package com.goodlike.inputparser;

import com.goodlike.config.Config;

public interface Parser {

    InputData parse(String file, Config config);

}
