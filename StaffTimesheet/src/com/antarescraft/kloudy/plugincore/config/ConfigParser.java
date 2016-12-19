package com.antarescraft.kloudy.plugincore.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * ConfigParser class
 * 
 * Utility class that parses a .yml configuration section
 */
public class ConfigParser
{
	/**
	 * @param section ConfigurationSection that is being parsed into a Java object
	 * @param classType Class Type of the java object that will be initialized and populated
	 * @return An instance of the class type passed into the function. The object will have its instance variables filled with defined config values
	 * @throws IOException 
	 */
	public static <T> T parse(ConfigurationSection section, Class<T> classType) throws ConfigurationParseException, IOException
	{
		return parse(section, classType, null, -1);
	}
	
	/**
	 * @param section ConfigurationSection that is being parsed into a Java object
	 * @param classType Class Type of the java object that will be initialized and populated
	 * @param docsFilepath The filepath where the function should save the config documentation text
	 * @return An instance of the class type passed into the function. The object will have its instance variables filled with defined config values
	 * @throws IOException 
	 */
	public static <T> T parse(ConfigurationSection section, Class<T> classType, String docsFilepath) throws ConfigurationParseException, IOException
	{
		return parse(section, classType, docsFilepath, -1);
	}
	
	/**
	 * @param section ConfigurationSection that is being parsed into a Java object
	 * @param classType Class Type of the java object that will be initialized and populated
	 * @param docsFilepath The filepath where the function should save the config documentation text
	 * @param docsIndentColumn The column in the text file you want the docs comments to start
	 * @return An instance of the class type passed into the function. The object will have its instance variables filled with defined config values
	 * @throws IOException 
	 */
	public static <T> T parse(ConfigurationSection section, Class<T> classType, String docsFilepath, int docsIndentColumn) throws ConfigurationParseException, IOException
	{
		StringBuilder docsBuilder = null;
		
		if(docsFilepath != null)
		{
			docsBuilder = new StringBuilder();
		}
		
		T object = parse(section, classType, docsBuilder, "", docsIndentColumn);
		
		if(docsFilepath != null)
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(docsFilepath)));
			bw.write(docsBuilder.toString());
			bw.flush();
			bw.close();
		}
		
		return object;
	}
	
	private static <T> T parse(ConfigurationSection section, Class<T> classType, StringBuilder docsBuilder, String indent, int docsIndentColumn)throws ConfigurationParseException
	{
		Set<String> keySet = section.getKeys(false);
			
		T obj = null;
		try 
		{
			Constructor<T> constructor = classType.getDeclaredConstructor();
			constructor.setAccessible(true);
			obj = constructor.newInstance();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			
			throw new ConfigurationParseException("Unable to create an instance of class " + classType.getName());
		}
		
		// Find the ConfigElementKey field if it exists and append its note
		Field keyField = ConfigParser.getConfigElementKeyField(classType);
		if(keyField != null)
		{
			keyField.setAccessible(true);
			try 
			{
				keyField.set(obj, section.getName());
			} 
			catch (IllegalArgumentException | IllegalAccessException e) 
			{
				throw new ConfigurationParseException("Unabled to set the element key field for class " + classType.getName());
			}
		
			if(!section.getName().equals("") && docsBuilder != null)//don't print root
			{
				//print config section name
				String currentLine = indent + section.getName() + ":";
				docsBuilder.append("\n" + currentLine);
				indent += "  ";
				
				keyField.setAccessible(true);
				try
				{
					keyField.set(obj, section.getName());
				} catch (IllegalArgumentException | IllegalAccessException e)
				{
					throw new ConfigurationParseException("Unable to set the element key field for class " + obj.getClass().getName());
				}
				
				boolean foundKeyAnnotation = false;
				
				// Attempt to find a matching ConfigElementKeyNote based on the current key of the config element
				// If a match can't be found, use the note attached to the ConfigElementKey annotation if it exists
				
				ConfigElementKeyNotes keyNoteAnnotations = keyField.getDeclaredAnnotation(ConfigElementKeyNotes.class);
				if(keyNoteAnnotations != null)
				{
					for(ConfigElementKeyNote keyNoteAnnotation : keyNoteAnnotations.value())
					{
						// Found the corresponding ConfigElementKeyNote annotation
						if(keyNoteAnnotation.key().equals(section.getName()))
						{
							docsBuilder.append(getDocsIndentSpaces(currentLine, docsIndentColumn) + "#  " + keyNoteAnnotation.note());
							foundKeyAnnotation = true;
						}
					}
				}
				// There could be just a single ConfigElementKeyNote attached to the field
				else
				{
					ConfigElementKeyNote keyNoteAnnotation = keyField.getDeclaredAnnotation(ConfigElementKeyNote.class);
					if(keyNoteAnnotation != null && 
							keyNoteAnnotation.key().equals(section.getName()))
					{
						docsBuilder.append(getDocsIndentSpaces(currentLine, docsIndentColumn) + "#  " + keyNoteAnnotation.note());
						foundKeyAnnotation = true;
					}
				}
				
				ConfigElementKey keyAnnotation = keyField.getDeclaredAnnotation(ConfigElementKey.class);
				
				// Didn't find a corresponding ConfigElementKeyNote annotation on the field
				// Print the ConfigElementKey annotation's note if it exists
				if(!foundKeyAnnotation && !keyAnnotation.note().equals(""))
				{
					docsBuilder.append(ConfigParser.getDocsIndentSpaces(currentLine, docsIndentColumn) + "#  " + keyAnnotation.note());
				}				
			
				
				docsBuilder.append("\n");
			}
		}
		
		for(Field field : classType.getDeclaredFields())
		{
			boolean fieldSet = false;
			boolean optional = false;
			
			field.setAccessible(true);
			
			if(!field.isAnnotationPresent(ConfigProperty.class))continue;
			
			writeDocs(field, section, docsBuilder, indent, docsIndentColumn);
			
			for(String key : keySet.toArray(new String[keySet.size()]))
			{
				try
				{
					// ConfigPropery Field
					ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
					if(configProperty.key().equals(key))
					{
						optional = (field.isAnnotationPresent(OptionalConfigProperty.class));

						System.out.println("config property field: " + field.getName());
						
						try
						{
							// ConfigElement field
							if(field.isAnnotationPresent(ConfigElement.class))
							{
								System.out.println("is config element");
								ConfigurationSection elementSection = section.getConfigurationSection(key);
								field.set(obj, parse(elementSection, field.getType(), docsBuilder, indent + "  ", docsIndentColumn));
							
								fieldSet = true;
								
								break;
							}
							// ConfigElementMap field
							else if(field.isAnnotationPresent(ConfigElementMap.class))
							{
								System.out.println("is element map");
								ParameterizedType genericMapType = (ParameterizedType) field.getGenericType();
								Class<?> mapType = (Class<?>) genericMapType.getActualTypeArguments()[1];//HashMap<String, ?>
								
								ConfigurationSection elementMapSection = section.getConfigurationSection(key);
								
								HashMap<String, Object> elements = new HashMap<String, Object>();
								for(String elementKey : elementMapSection.getKeys(false))
								{
									ConfigurationSection elementSection = elementMapSection.getConfigurationSection(elementKey);
									
									elements.put(elementKey, parse(elementSection, mapType, docsBuilder, indent + "  ", docsIndentColumn));
								}
								
								System.out.println("map field: " + field.getName() + " elements: " + elements);
														
								try 
								{
									field.set(obj, elements);
									
									fieldSet = true;
									
									break;
								}
								catch (IllegalArgumentException e) 
								{
									e.printStackTrace();
									throw new ConfigurationParseException(String.format("Field %s in class %s must be of type Map<String, ?> with the appropriate generic type to hold the ConfigurationElementMap", 
											field.getName(), classType.getName()));
								}
							}
							// Primitive field
							else
							{
								System.out.println("is primitive");
								// Check to make sure the value of the config property is between the min-max range defined by the annotation
								Annotation primitiveAnnotation = findPrimitiveAnnotationClassByFieldType(field);
								if(primitiveAnnotation != null && primitiveAnnotation.annotationType().isAnnotationPresent(RangedConfigProperty.class))
								{
									try
									{
										Number value = (Number) section.get(key);
										
										Method minValue = primitiveAnnotation.annotationType().getDeclaredMethod("minValue");
										Method maxValue = primitiveAnnotation.annotationType().getDeclaredMethod("maxValue");
										
										Number minNumber = (Number) minValue.invoke(primitiveAnnotation);
										Number maxNumber = (Number) maxValue.invoke(primitiveAnnotation);
										
										// Value from the config does not respect the min/max value range, throw exception
										if(compareGenericNumbers(value, minNumber) < 0 || compareGenericNumbers(value, maxNumber) > 0)
										{
											String keyName  = new String();
											if(section.getName().equals(""))// Root, don't include this with the key name
											{
												keyName = key;
											}
											else
											{
												keyName = section.getName() + "." + key;
											}
											throw new ConfigurationParseException(String.format("Value in config field '%s' is outside the min / max range specified for this property.", 
													keyName));
										}
									}
									catch(Exception e){}
								}
								
								field.set(obj, section.get(key));
								
								fieldSet = true;
								
								break;
							}
						}
						catch(IllegalArgumentException e)
						{
							e.printStackTrace();
							
							throw new ConfigurationParseException(String.format("Type mismatch occured on field '%s' in class '%s' for config propery '%s'",
									field.getName(), classType.getName(), section.getName() + "." + key));
						}
					}
				}
				catch(IllegalAccessException ex)
				{
					ex.printStackTrace();
					
					throw new ConfigurationParseException(String.format("Unable to access field %s in class %s", 
							field.getName(), classType.getName()));
				}
			}
			
			if(!fieldSet && !optional && !field.isAnnotationPresent(ConfigElementKey.class))
			{
				throw new ConfigurationParseException(String.format("No config property in the config file was found to match the required config property field '%s' in class '%s'", 
						field.getName(), classType.getName()));
			}
		}

		return obj;
	}
	
	private static int compareGenericNumbers(Number a, Number b)
	{
        return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
    }
	
	/**
	 * Attempts to find a corresponding annotation type based on the class type of the field
	 * @param field The field whose annotations are being searched
	 * @return The corresponding annotation class based on the field's class type. If the annotation class coudn't be found, return null
	 */
	private static Annotation findPrimitiveAnnotationClassByFieldType(Field field)
	{
		if(!field.getType().isPrimitive() && !field.getType().equals(String.class)) return null;
		
		String type = field.getType().getSimpleName();
		type = type.substring(0, 1).toUpperCase() + type.substring(1);//Capitalize first letter of the type
				
		for(Annotation annotation : field.getDeclaredAnnotations())
		{
			//Annotation class is a primitive ConfigProperty and is the corresponding type
			if(annotation.annotationType().getSimpleName().equals(type + "ConfigProperty"))
			{
				return annotation;
			}
		}
		
		return null;
	}
	
	private static Field getConfigElementKeyField(Class<?> classType)
	{		
		for(Field field : classType.getDeclaredFields())
		{
			if(field.isAnnotationPresent(ConfigElementKey.class))
			{
				return field;
			}
		}
		
		return null;
	}
	
	private static String getDocsIndentSpaces(String currentLine, int docsIndentColumn)
	{
		String spaces = new String();
		int diff = docsIndentColumn - currentLine.length();
		if(diff < 0) return " ";
		
		for(int i = 0; i < diff; i++)
		{
			spaces += " ";
		}
		
		return spaces;
	}
	
	private static StringBuilder writeDocs(Field field, ConfigurationSection section, StringBuilder docsBuilder, String indent, int docsIndentColumn)
	{
		if(docsBuilder == null) return null;
		
		// ConfigElementKey field, skip
		if(field.isAnnotationPresent(ConfigElementKey.class))
		{
			return docsBuilder;
		}
		
		ConfigProperty configProperty = field.getDeclaredAnnotation(ConfigProperty.class);
		if(configProperty != null)
		{
			String key = configProperty.key();
			
			try
			{
				String defaultValueString = null;
				String minValueString = null;
				String maxValueString = null;
				
				// attempt to find the corresponding primitive annotation class based on the field's class type
				Annotation primitiveAnnotation = findPrimitiveAnnotationClassByFieldType(field);
				if(primitiveAnnotation != null)
				{
					// Use reflection to fetch defaultValue, minValue, and maxValue from the field annotation
					try
					{
						Method defaultValue = primitiveAnnotation.annotationType().getDeclaredMethod("defaultValue");
						defaultValueString = defaultValue.invoke(primitiveAnnotation).toString();
					}
					catch(Exception e){}
					
					try
					{
						Method minValue = primitiveAnnotation.annotationType().getDeclaredMethod("minValue");
						minValueString = minValue.invoke(primitiveAnnotation).toString();
					}
					catch(Exception e){}
					
					try
					{
						Method maxValue = primitiveAnnotation.annotationType().getDeclaredMethod("maxValue");
						maxValueString = maxValue.invoke(primitiveAnnotation).toString();
					}
					catch(Exception e){}
				}
				
				boolean optional = field.isAnnotationPresent(OptionalConfigProperty.class);
				String note = configProperty.note();

				String propertyNote = "#  (Required: " + !optional;
				if(defaultValueString != null) propertyNote += ", Default: " + defaultValueString;
				if(minValueString != null) propertyNote += ", Min: " + minValueString;
				if(maxValueString != null) propertyNote += ", Max: " + maxValueString;
				propertyNote += ") " + note;
				
				// simple config property
				if(primitiveAnnotation != null)
				{
					String currentLine = indent + key + ": " + section.get(key).toString();
					docsBuilder.append(currentLine + getDocsIndentSpaces(currentLine, docsIndentColumn) + propertyNote);
				}
				// complex config property
				else
				{
					String currentLine = indent + key + ":";
					docsBuilder.append(currentLine + getDocsIndentSpaces(currentLine, docsIndentColumn) + propertyNote);
				}

				docsBuilder.append("\n");
				
				//write list values to docs
				if(field.isAnnotationPresent(ListConfigProperty.class))
				{
					for(Object listObj : section.getList(key))
					{
						docsBuilder.append(indent + "  -  " + listObj.toString() + "\n");
					}
				}
			} 
			catch (Exception e)
			{
				//Annotation on the field is not a ConfigParser annotation
			}
		}
		
		return docsBuilder;
	}
	
	/**
	 * Saves the instance fields of the input object to the configuration file at the specified path
	 * 
	 * @param yaml YamlConfiguration that will be updated with the input Object's instance field values
	 * @param path Configuration path in the yaml file that the input objects fields should be saved
	 * @param object The Object whose instance fields will be saved into the yaml file at the specified path
	 * @throws IOException 
	 * @throws ConfigurationParseException 
	 */
	public static void saveObject(File yamlFile, String path, Object object) throws IOException
	{
		if(!yamlFile.exists())
		{
			yamlFile.createNewFile();
		}
		
		YamlConfiguration yaml = YamlConfiguration.loadConfiguration(yamlFile);
		
		if(object == null)
		{
			yaml.set(path, null);
		}
		else
		{
			ConfigurationSection section = yaml.createSection(path);
			
			saveObject(section, object);
			
			yaml.set(path, section);
			yaml.save(yamlFile);
		}
	}
	
	private static void saveObject(ConfigurationSection section, Object object) throws IOException
	{
		for(Field field : object.getClass().getDeclaredFields())
		{
			field.setAccessible(true);
			
			ConfigProperty configAnnotation = field.getDeclaredAnnotation(ConfigProperty.class);
			if(configAnnotation != null)
			{
				try
				{
					if(field.isAnnotationPresent(ConfigElement.class))
					{
						ConfigurationSection elementSection = section.createSection(configAnnotation.key());
						saveObject(elementSection, field.get(object));
					}
					else if(field.isAnnotationPresent(ConfigElementMap.class))
					{
						ConfigurationSection mapSection = section.createSection(configAnnotation.key());
						for(String mapKey : mapSection.getKeys(false))
						{
							ConfigurationSection elementSection = mapSection.createSection(mapKey);
							saveObject(elementSection, field.get(object));
						}
					}
					else
					{
						section.set(configAnnotation.key(), field.get(object));
					}
				}
				catch (IllegalArgumentException | IllegalAccessException e) {e.printStackTrace();}
			}
		}
	}
}