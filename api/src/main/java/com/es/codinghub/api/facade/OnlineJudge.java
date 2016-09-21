package com.es.codinghub.api.facade;

import java.io.IOException;

public enum OnlineJudge {

	Codeforces {
		@Override
		public OnlineJudgeApi getApi() throws IOException {
			return new Codeforces();
		}
	},

	UVa {
		@Override
		public OnlineJudgeApi getApi() throws IOException {
			return new UVa();
		}
	};

	public abstract OnlineJudgeApi getApi() throws IOException;
}
