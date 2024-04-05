//@TypeDef(name = "type-stripped-string", defaultForType = String.class, typeClass = StringStripType.class)

@GenericGenerators({
        @GenericGenerator(name = "seq_gen_person", type = SequenceStyleGenerator.class,
                parameters = {@Parameter(name = "sequence_name", value = "PERSON_SEQ"), @Parameter(name = "increment_size", value = "1")}),
        @GenericGenerator(name = "seq_gen_address", type = SequenceStyleGenerator.class,
                parameters = {@Parameter(name = "sequence_name", value = "ADDRESS_SEQ"), @Parameter(name = "increment_size", value = "1")}),
        @GenericGenerator(name = "seq_gen_test", type = BlockSequenceGenerator.class,
                parameters = {@Parameter(name = "sequenceName", value = "ADDRESS_SEQ"), @Parameter(name = "blockSize", value = "5")})
})

@ConverterRegistrations({
        @ConverterRegistration(converter = StringStripConverter.class),
        @ConverterRegistration(converter = ColorConverter.class)
})
package de.freese.jpa.model;
//
// import org.hibernate.annotations.TypeDef;

import org.hibernate.annotations.ConverterRegistration;
import org.hibernate.annotations.ConverterRegistrations;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.GenericGenerators;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import de.freese.jpa.BlockSequenceGenerator;
import de.freese.jpa.utils.ColorConverter;
import de.freese.jpa.utils.StringStripConverter;
