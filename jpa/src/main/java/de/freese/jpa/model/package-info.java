//@TypeDef(name = "type-stripped-string", defaultForType = String.class, typeClass = StringStripType.class)

// @SequenceGenerators({
//         @SequenceGenerator(name = "seq_gen_person", sequenceName = "PERSON_SEQ", allocationSize = 1),
//         @SequenceGenerator(name = "seq_gen_address", sequenceName = "ADDRESS_SEQ", allocationSize = 1)
// })

// @GenericGenerators({
//         @GenericGenerator(name = "seq_gen_person", type = SequenceStyleGenerator.class,
//                 parameters = {@Parameter(name = "sequence_name", value = "PERSON_SEQ"), @Parameter(name = "increment_size", value = "1")}),
//         @GenericGenerator(name = "seq_gen_address", type = SequenceStyleGenerator.class,
//                 parameters = {@Parameter(name = "sequence_name", value = "ADDRESS_SEQ"), @Parameter(name = "increment_size", value = "1")}),
//         @GenericGenerator(name = "seq_gen_test", type = BlockSequenceGenerator.class,
//                 parameters = {@Parameter(name = "sequenceName", value = "ADDRESS_SEQ"), @Parameter(name = "blockSize", value = "5")})
// })

@ConverterRegistrations({
        @ConverterRegistration(converter = StringStripConverter.class),
        @ConverterRegistration(converter = ColorConverter.class)
})
package de.freese.jpa.model;
//
// import org.hibernate.annotations.TypeDef;

import org.hibernate.annotations.ConverterRegistration;
import org.hibernate.annotations.ConverterRegistrations;

import de.freese.jpa.converter.ColorConverter;
import de.freese.jpa.converter.StringStripConverter;
