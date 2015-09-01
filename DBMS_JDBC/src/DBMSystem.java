public class DBMSystem implements dbms {
	
	private Validator Validate = new Validator();
	public Parser parser;
	
	@Override
	public String input(String input) throws Exception{
		if(!Validate.Validate_Expression(input))
			return dbms.PARSING_ERROR;
		parser = new Parser();
		return parser.perform(input).trim();
	}

}