/*******************************************************************************
 * This file was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 N'yoma Diamond
 *******************************************************************************/

package escape;

import java.io.*;
import javax.xml.bind.*;
import javax.xml.transform.stream.StreamSource;
import org.antlr.v4.runtime.CharStreams;
import econfig.EscapeConfigurator;
import escape.alpha.EscapeGameManagerImpl;
import escape.util.EscapeGameInitializer;


/**
 * This class builds an instance of an EscapeGameManager from a configuration
 * file (.egc). This uses the EscapeConfigurator for XML to turnthe .egc file
 * into a valid XML string that is then unmarshalled into an EscapeGameInitializer
 * file.
 * 
 * MODIFIABLE: YES
 * MOVEABLE: NO
 * REQUIRED: YES
 * 
 * >>>THIS IS AN EXAMPLE STARTER FILE<<<
 * You must change this class to be able to get the data from the configurtion
 * file and implement the makeGameManager() method. You may not change the signature
 * of that method or the constructor for this class. You can change the file any 
 * other way that you need to.
 * 
 * You do not have to use JAXB to create XML. You might use GSON if you are more
 * familiar with it. You also don't have to use the EscapeGameInitializer object if
 * you have a way that better suits your design and capabilities. Don't go down
 * a rathole, however, in order to use something different. This implementation
 * works and will not take much time to modify the EscapeGameInitializer to create
 * your game instance.
 */
public class EscapeGameBuilder {
	private final EscapeGameInitializer gameInitializer;

	/**
	 * The constructor takes a file that points to the Escape game
	 * configuration file. It should get the necessary information 
	 * to be ready to create the game manager specified by the configuration
	 * file and other configuration files that it links to.
	 * @param fileName the file for the Escape game configuration file (.egc).
	 * @throws Exception on any errors
	 */
	public EscapeGameBuilder(String fileName) throws Exception {
		String xmlConfiguration = getXmlConfiguration(fileName);
		// Uncomment the next instruction if you want to see the XML
		System.out.println(xmlConfiguration);
		gameInitializer = unmarshalXml(xmlConfiguration);
	}

	/**
	 * Take the .egc file contents and turn it into XML.
	 * If you want to use JSON, you should change the name of this method and 
	 * initizlize the EscapeConfigurator with this code:
	 * 		EscapeConfigurator configurator = new EscapeConfigurator(EscapeConfigurationJsonMaker());
	 * @param fileName
	 * @return the XML data needed to 
	 * @throws IOException
	 */
	private String getXmlConfiguration(String fileName) throws IOException {
		EscapeConfigurator configurator = new EscapeConfigurator();
		return configurator.makeConfiguration(CharStreams.fromFileName(fileName));
	}

	/**
	 * Unmarshal the XML into an EscapeGameInitializer object.
	 * @param xmlConfiguration
	 * @throws JAXBException
	 */
	private EscapeGameInitializer unmarshalXml(String xmlConfiguration) throws JAXBException {
		JAXBContext contextObj = JAXBContext.newInstance(EscapeGameInitializer.class);
		Unmarshaller mub = contextObj.createUnmarshaller();
		return (EscapeGameInitializer) mub.unmarshal(
			new StreamSource(new StringReader(xmlConfiguration))
		);
	}

	/**
	 * Getter for the gameInitializer. Can be used to examine it after the builder
	 * creates it.
	 * @return the gameInitializer
	 */
	public EscapeGameInitializer getGameInitializer() {
		return gameInitializer;
	}

	/**
	 * Once the builder is constructed, this method creates the
	 * EscapeGameManager instance. For this example, you would use the
	 * gameInitializer object to get all of the information you need to create
	 * your game.
	 * @return the game instance
	 */
	public EscapeGameManager makeGameManager() {
		// >>> YOU MUST IMPLEMENT THIS METHOD<<<
		return new EscapeGameManagerImpl(gameInitializer);
	}
}
